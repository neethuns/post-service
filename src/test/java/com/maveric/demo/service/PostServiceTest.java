package com.maveric.demo.service;

import com.maveric.demo.constant.PostConstant;
import com.maveric.demo.dto.PostDto;
import com.maveric.demo.dto.PostRequest;
import com.maveric.demo.dto.UserDto;
import com.maveric.demo.exception.CustomFeignException;
import com.maveric.demo.exception.PostNotFoundException;
import com.maveric.demo.feign.CommentFeign;
import com.maveric.demo.feign.LikeFeign;
import com.maveric.demo.feign.UserFeign;
import com.maveric.demo.model.Post;
import com.maveric.demo.repo.PostRepo;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PostServiceTest {

   @InjectMocks
    PostServiceImpl postService;
    @Mock
    PostRepo postRepo;
    @Mock
    UserFeign userFeign;
    @Mock
    CommentFeign commentFeign;
    @Mock
    LikeFeign likeFeign;

    @Test
    void testDeletePost() {
        Post post=new Post();
        post.setCreatedAt(LocalDateTime.now());
        post.setPostId("1");
        post.setPost("Post1");
        post.setPostedBy("12");
        post.setUpdatedAt(LocalDateTime.now());
        Mockito.when(this.postRepo.findById("1")).thenReturn(Optional.of(post));
       postService.deletePost("1");
       // when(this.postService.deletePost("1")).thenReturn(PostConstant.DELETEPOST);
       verify(postRepo, times(1)).deleteById("1");
    }

//    @Test
//    void testExceptionThrownWhenIdNotFound() {
//        Mockito.doThrow(PostNotFoundException.class).when(postRepo).deleteById(any());
//        Exception userNotFoundException = assertThrows(PostNotFoundException.class, () -> postService.deletePost("1"));
//        assertTrue(userNotFoundException.getMessage().contains(PostConstant.POSTNOTFOUND));
//    }

    @Test
    void testCreatePost() {
        UserDto userDto=new UserDto("12","First","Second","Third","1",LocalDate.of(1989,10,13),"MALE","1","O_POS","anug@mail.com","chennai");
        Mockito.when(userFeign.getUserDetails((String) any())).thenReturn(userDto);
         Post post = new Post();
        post.setCreatedAt(LocalDateTime.now());
        post.setPostId("1");
        post.setPost("Post1");
        post.setPostedBy("12");
        post.setUpdatedAt(LocalDateTime.now());
        post.setLikescount(0L);
        post.setCommentscount(0l);
        PostDto postDto=new PostDto("1","Post1",userDto,LocalDateTime.now(),LocalDateTime.now(),0l,0l);
       Mockito.when(postRepo.save((Post) any())).thenReturn(post);

        when(postService.createPost(post)).thenReturn(postDto);
       // assertEquals("Post1",postService.createPost(post).getPost());
        assertEquals("Post1",postDto.getPost());
        assertEquals(post.getPostedBy(),postDto.getPostedBy().getUserId());
    }

    @Test
    void testGetPostDetails() {
        UserDto userDto=new UserDto("12","First","Second","Third","1",LocalDate.of(1989,10,13),"MALE","1","O_POS","anug@mail.com","chennai");
        Mockito.when(userFeign.getUserDetails(any())).thenReturn(userDto);
        Mockito.when(commentFeign.getCommentsCounts(any())).thenReturn(2L);
        Mockito.when(likeFeign.getLikesCounts(any())).thenReturn(3L);
        Post post = new Post();
        post.setPostId("1");
        post.setPost("Hi");
        post.setPostedBy("12");

        post.setLikescount(3L);
        post.setCommentscount(2l);
        PostDto postDto=new PostDto("1","Hi",userDto,null,null,3l,2l);

        Mockito.when(postRepo.findById("1")).thenReturn(Optional.of(post));
       // when(postService.getPostDetails(post.getPostId())).thenReturn(postDto);
       assertThat(postService.getPostDetails("1")).isEqualTo(postDto);
        assertEquals(post.getPost(),postDto.getPost());
      }
//
    @Test
    void testExceptionThrownWhenFeignConnectionIssueInGetPostDetailByID() {
        when(this.userFeign.getUserDetails((String) any())).thenReturn(new UserDto());

        Post post = new Post();
        post.setCreatedAt(LocalDateTime.now());
        post.setPostId("1");
        post.setPost("Post");
        post.setPostedBy("Posted By");
        post.setUpdatedAt(LocalDateTime.now());
        Optional<Post> ofResult = Optional.of(post);
        when(postRepo.findById((String) any())).thenReturn(ofResult);
        when(this.likeFeign.getLikesCounts((String) any())).thenReturn(3L);
        when(this.commentFeign.getCommentsCounts((String) any())).thenThrow(mock(FeignException.class));
        assertThrows(CustomFeignException.class, () -> this.postService.getPostDetails("1"));
    }

    @Test
    void testUpdatePost() {
        Post post=new Post();
        post.setPostId("1");
        post.setPost("Hi All");
        post.setPostedBy("12");
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        Post post1 = createOnePost();
        PostDto postDto = createOnePostToResponse();
        Mockito.when(postRepo.findById("1")).thenReturn(Optional.of(post1));
        when(postService.updatePost(post,"1")).thenReturn(postDto);
        assertEquals(post.getPost(),postDto.getPost());


    }
//
//    @Test
//    void testExceptionThrownWhenFeignConnectionIssueInUpdatePost() {
//        when(this.userFeign.getUserDetails((String) any())).thenReturn(new UserDto());
//
//        Post post = new Post();
//        post.setCreatedAt(LocalDateTime.now());
//        post.setPostId("1");
//        post.setPost("Post");
//        post.setPostedBy("Posted By");
//        post.setUpdatedAt(LocalDateTime.now());
//        Optional<Post> ofResult = Optional.of(post);
//
//
//        Post post1 = new Post();
//        post1.setCreatedAt(LocalDateTime.now());
//        post1.setPostId("1");
//        post1.setPost("Post");
//        post1.setPostedBy("Posted By");
//        post1.setUpdatedAt(LocalDateTime.now());
//        when(postRepo.save(post)).thenReturn(post);
//        when(postRepo.findById((String) any())).thenReturn(ofResult);
//        when(likeFeign.getLikesCounts((String) any())).thenReturn(3L);
//        when(commentFeign.getCommentsCounts((String) any())).thenThrow(mock(CustomFeignException.class));
//        assertThrows(CustomFeignException.class, () -> this.postService.updatePost(post,"1"));
//    }

    @Test
    void testGetAllPosts() {
        Mockito.when(userFeign.getUserDetails((String) any())).thenReturn(new UserDto());
        Mockito.when(likeFeign.getLikesCounts((String) any())).thenReturn(3l);
        Mockito.when(commentFeign.getCommentsCounts((String) any())).thenReturn(3l);
        PostDto postDto=createOnePostDto();
        List<PostDto> postDtoList=new ArrayList<>();
        postDtoList.add(postDto);
        Post post = new Post();
        post.setCreatedAt(LocalDateTime.now());
        post.setPostId("1");
        post.setPost("Hi All");
        post.setPostedBy("12");
        post.setUpdatedAt(LocalDateTime.now());

        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post);
        PageImpl<Post> pageImpl = new PageImpl<>(postList);
        Mockito. when(postRepo.findAll((org.springframework.data.domain.Pageable) any())).thenReturn(pageImpl);

       // when(postService.getPosts(1, 3)).thenReturn(postDtoList);
        assertEquals(1,postService.getPosts(1, 3).size() );
    }

 //   @Test
//    void testExceptionThrownWhenFeignConnectionFailed() {
//        when(this.userFeign.getUserById((String) any())).thenReturn(new UserDto());
//
//        Post post = new Post();
//        post.setCreatedAt(LocalDate.now());
//        post.setId("1");
//        post.setPost("Post");
//        post.setPostedBy("Posted By");
//        post.setUpdatedAt(LocalDate.now());
//
//        ArrayList<Post> postList = new ArrayList<>();
//        postList.add(post);
//        PageImpl<Post> pageImpl = new PageImpl<>(postList);
//        when(this.postRepo.findAll((org.springframework.data.domain.Pageable) any())).thenReturn(pageImpl);
//        when(this.likeFeign.getLikesCount((String) any())).thenReturn(3);
//        when(this.commentFeign.getCommentsCount((String) any())).thenThrow(mock(FeignException.class));
//        assertThrows(CustomFeignException.class, () -> this.postService.getPosts(1, 3));
//    }

//    private PostRequest createOnePostToRequest() {
//        PostRequest postRequest = new PostRequest();
//        postRequest.setPost("Hi");
//        postRequest.setPostedBy("2");
//        return postRequest;
//    }

    private Post createOnePost() {
        Post post1 = new Post();
        post1.setPostId("1");
        post1.setPost("Hi");
        post1.setCreatedAt(LocalDateTime.now());
        post1.setUpdatedAt(LocalDateTime.now());
        post1.setLikescount(0L);
        post1.setCommentscount(0l);
        return post1;
    }
//
    private PostDto createOnePostToResponse() {
        PostDto postDto = new PostDto();
        postDto.setPostId("1");
        postDto.setPost("Hi All");
        postDto.setPostedBy(userFeign.getUserDetails("12"));
        postDto.setCreatedAt(LocalDateTime.now());
        postDto.setUpdatedAt(LocalDateTime.now());
        return postDto;
    }
    private PostDto createOnePostDto() {
        PostDto postDto = new PostDto();
        postDto.setPostId("1");
        postDto.setPost("Hi All");
        postDto.setPostedBy(userFeign.getUserDetails("1"));
        postDto.setCreatedAt(LocalDateTime.now());
        postDto.setUpdatedAt(LocalDateTime.now());
        postDto.setCommentsCount(commentFeign.getCommentsCounts("1"));
        postDto.setLikesCount(likeFeign.getLikesCounts("1"));
        return postDto;
    }
}