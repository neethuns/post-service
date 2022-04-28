package com.maveric.demo.controller;

import com.maveric.demo.dto.PostDto;
import com.maveric.demo.model.Post;
import com.maveric.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.Optional;

@CrossOrigin(value = "*")
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @Autowired PostService postService;

//    @GetMapping
//    public ResponseEntity<List<Post>> getPosts() {
//        return new ResponseEntity<> (postService.getPosts(), HttpStatus.OK);
//    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getPosts(@QueryParam("page") Integer page, @QueryParam("pageSize") Integer pageSize)
    {
        return new ResponseEntity<>(postService.getPosts(page, pageSize), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody Post post)
    {
        return new ResponseEntity<> (postService.createPost(post), HttpStatus.CREATED);

    }

    @GetMapping("/{postId}")
    public ResponseEntity <PostDto> getPostDetails(@PathVariable("postId") String postId) {

        return new ResponseEntity<>(postService.getPostDetails(postId), HttpStatus.OK);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@RequestBody Post post, @PathVariable("postId") String postId )
    {
        return new ResponseEntity<> (postService.updatePost(post, postId), HttpStatus.OK);

    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("postId") String postId) {
        return new ResponseEntity<> (postService.deletePost(postId), HttpStatus.OK);
    }

}
