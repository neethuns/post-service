package com.maveric.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    @Id
    private String postId;
    private String post;
    private UserDto postedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long likesCount=0L;
    private Long commentsCount=0L;


}
