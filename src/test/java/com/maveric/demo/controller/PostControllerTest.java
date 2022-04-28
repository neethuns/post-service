package com.maveric.demo.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.maveric.demo.constant.PostConstant;
//import com.maveric.demo.constant.UserConstant;
import com.maveric.demo.dto.PostDto;
import com.maveric.demo.dto.UserDto;
//import com.maveric.demo.enums.BloodGroup;
//import com.maveric.demo.enums.Gender;
import com.maveric.demo.model.Post;
//import com.maveric.demo.model.User;
import com.maveric.demo.service.PostService;
//import com.maveric.demo.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//import static com.maveric.demo.enums.BloodGroup.B_POS;
//import static com.maveric.demo.enums.Gender.FEMALE;
//import static com.maveric.demo.enums.Gender.MALE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WebMvcTest(PostController.class)
public class PostControllerTest {


    @MockBean
    PostService postService;

    @Autowired
    MockMvc mockMvc;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetPosts()throws Exception {
        List<PostDto> postDto = createPostsList();

        Mockito.when(postService.getPosts(1, 2)).thenReturn(postDto);

        mockMvc.perform(get("/api/v1/posts?page=1&pageSize=2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].post", Matchers.is("FirstPost")))
                .andExpect(jsonPath("$[1].post", Matchers.is("SecondPost")));
    }

    private List<PostDto> createPostsList() {
        List<PostDto> posts = new ArrayList<>();

        PostDto post1 = new PostDto();
        post1.setPostId("1");
        post1.setPost("FirstPost");
        post1.setPostedBy(new UserDto("123","firstTest","middleTest","lastTest","123",LocalDate.now(),"FEMALE","123","B_POS","aug@gmail.com","Bangalore"));
        post1.setCreatedAt(null);
        post1.setUpdatedAt(null);
        post1.setLikesCount(3L);
        post1.setCommentsCount(2L);

        PostDto post2 = new PostDto();
        post2.setPostId("2");
        post2.setPost("SecondPost");
        post2.setPostedBy(new UserDto("123","firstTest","middleTest","lastTest","123",LocalDate.now(),"FEMALE","123","B_POS","aug@gmail.com","Bangalore"));
        post2.setCreatedAt(null);
        post2.setUpdatedAt(null);
        post2.setLikesCount(3L);
        post2.setCommentsCount(2L);

        posts.add(post1);
        posts.add(post2);
        return posts;
    }

    @Test
    public void testCreatePost() throws Exception {
        Post post = createOnePostToPost();
        PostDto postDto = new PostDto();
        Mockito.when(postService.createPost(post)).thenReturn(postDto);
        mockMvc.perform(post("/api/v1/posts")
                        .content(asJsonString(post))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    private Post createOnePostToPost() {
        Post post = new Post();
        post.setPostId("1");
        post.setPost("Hi");
        post.setPostedBy(String.valueOf(new UserDto("123","firstTest","middleTest","lastTest","123",LocalDate.now(),"FEMALE","123","B_POS","aug@gmail.com","Bangalore")));
        return post;
    }

    @Test
    public void testGetPostDetails() throws Exception {
        PostDto postDto = createOnePost();

        Mockito.when(postService.getPostDetails("1")).thenReturn(postDto);

        mockMvc.perform(get("/api/v1/posts/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.aMapWithSize(7)))
                .andExpect(jsonPath("$.post", Matchers.is("PostTest")));
    }

    private PostDto createOnePost() {
        PostDto postDto = new PostDto();
        postDto.setPostId("2");
        postDto.setPost("PostTest");
        postDto.setPostedBy(new UserDto("123","firstTest","middleTest","lastTest","123",LocalDate.now(),"FEMALE","123","B_POS","aug@gmail.com","Bangalore"));
        return postDto;
    }

    @Test
    public void testUpdatePost() throws Exception {
        Post post = createOnePostToUpdate();
        PostDto postDto = new PostDto();

        Mockito.when(postService.updatePost(post,"123")).thenReturn(postDto);
        mockMvc.perform(put("/api/v1/posts/1")
                        .content(asJsonString(post))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
    private Post createOnePostToUpdate() {
        Post post = new Post();
        post.setPostId("1");
        post.setPost("PostTest");
        post.setPostedBy(String.valueOf(new UserDto("123","firstTest","middleTest","lastTest","123",LocalDate.now(),"FEMALE","123","B_POS","aug@gmail.com","Bangalore")));
           /*postDto.setCreatedAt(null);
        postDto.setUpdatedAt(null);
        postDto.setLikesCount(5);
        postDto.setCommentsCount(3);*/

        return post;
    }

    @Test
    public void testDeletePost() throws Exception {

        Mockito.when(postService.deletePost("1")).thenReturn(PostConstant.DELETEPOST);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
