package com.maveric.demo.service;

import com.maveric.demo.dto.PostDto;
import com.maveric.demo.dto.UserDto;
import com.maveric.demo.exception.CustomFeignException;
import com.maveric.demo.exception.PostNotFoundException;
import com.maveric.demo.feign.CommentFeign;
import com.maveric.demo.feign.LikeFeign;
import com.maveric.demo.feign.UserFeign;
import com.maveric.demo.model.Post;
import com.maveric.demo.repo.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.maveric.demo.constant.PostConstant.*;

@Service
public class PostServiceImpl implements PostService{


    @Autowired
    PostRepo postRepo;

    @Autowired
    CommentFeign commentFeign;

    @Autowired
    LikeFeign likeFeign;

    @Autowired
    UserFeign userFeign;

//    @Override
//    public List<Post> getPosts() {
//        return postRepo.findAll();
//    }

    @Override
    public List<PostDto> getPosts(Integer page, Integer pageSize) {

        try {
            if (page == null) {
                page = 1;
            }
            if (pageSize == null) {
                pageSize = 10;
            }
            Page<Post> posts = postRepo.findAll(PageRequest.of(page - 1, pageSize));
//        List<Post> postList= postRepo.findAll();
            List<PostDto> postLists = new ArrayList<>();
            if (posts.isEmpty()) {
                throw new PostNotFoundException(POSTNOTFOUND);
            } else {
                String postId;
                for (Post post : posts) {
                    postId = post.getPostId();
                    post.setCommentscount(commentFeign.getCommentsCounts(postId));
                    post.setLikescount(likeFeign.getLikesCounts(postId));
                    UserDto userDto = userFeign.getUserDetails(post.getPostedBy());

                    postLists.add(new PostDto(post.getPostId(), post.getPost(), userDto, post.getCreatedAt(), post.getUpdatedAt(), post.getCommentscount(), post.getLikescount()));
                }

                return postLists;
            }
        }  catch (feign.FeignException e)
        {
            throw new CustomFeignException(FEIGNEXCEPTON);
        }

    }

    @Override
    public PostDto createPost(Post post) {

        try {
            post.setCreatedAt(LocalDateTime.now());
            post.setUpdatedAt(LocalDateTime.now());

            UserDto userDto = userFeign.getUserDetails(post.getPostedBy());
            postRepo.save(post);
            return new PostDto(post.getPostId(), post.getPost(), userDto, post.getCreatedAt(), post.getUpdatedAt(), post.getCommentscount(), post.getLikescount());
        } catch (feign.FeignException e) {
            throw new CustomFeignException(FEIGNEXCEPTON);
        }
    }


    public PostDto getPostDetails(String postId)
    {try {
        Optional<Post> post = postRepo.findById(postId);
        if (post.isPresent()) {
            Post posts = post.get();
            UserDto userDto = userFeign.getUserDetails(posts.getPostedBy());
            posts.setCommentscount(commentFeign.getCommentsCounts(postId));
            posts.setLikescount(likeFeign.getLikesCounts(postId));

            PostDto postDto = new PostDto(posts.getPostId(), posts.getPost(), userDto, posts.getCreatedAt(), posts.getUpdatedAt(), posts.getLikescount(), posts.getCommentscount());
            return postDto;
        } else {
            throw new PostNotFoundException(POSTNOTFOUND + postId);
        }
    } catch (feign.FeignException e)
    {
        throw new CustomFeignException(FEIGNEXCEPTON);
    }
    }

    @Override
    public PostDto updatePost(Post post, String postId) {
try {
    if (postId.equals(post.getPostId())) {
        Optional<Post> selectedPost = postRepo.findById(postId);
        if (selectedPost.isPresent()) {
            Post postUpdate = selectedPost.get();
            postUpdate.setPost(post.getPost());
            postUpdate.setUpdatedAt(LocalDateTime.now());
            UserDto userDto = userFeign.getUserDetails(post.getPostedBy());
            postRepo.save(postUpdate);

            return new PostDto(postUpdate.getPostId(), postUpdate.getPost(), userDto, postUpdate.getCreatedAt(), postUpdate.getUpdatedAt(), postUpdate.getCommentscount(), postUpdate.getLikescount());
        } else throw new PostNotFoundException(POSTNOTFOUND + postId);
    } else {
        throw new PostNotFoundException(POSTIDMISMATCH);
    }
} catch (feign.FeignException e)
{
    throw new CustomFeignException(FEIGNEXCEPTON);
}
    }

    @Override
    public String deletePost(String id) {
        Optional<Post> postSelected = postRepo.findById(id);
        if(postSelected.isPresent())
        {
            postRepo.deleteById(id);
            return DELETEPOST + id;
        }
        else
        {
            throw new PostNotFoundException(POSTNOTFOUND + id);
        }
    }
}
