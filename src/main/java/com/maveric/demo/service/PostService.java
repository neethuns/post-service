package com.maveric.demo.service;

import com.maveric.demo.dto.PostDto;
import com.maveric.demo.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {

    List<PostDto> getPosts(Integer page, Integer pageSize);
    PostDto createPost(Post post);
   PostDto getPostDetails(String postId);

    PostDto updatePost(Post post, String postedBy);

    String deletePost(String id);

}
