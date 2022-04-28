package com.maveric.demo.model;

import com.maveric.demo.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Document(collection="post")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Post {

        @Id
        private String postId;
        @NotNull(message="PostedBy can not be null")
        private String postedBy;
        @NotNull(message="Post can not be null")
        private String post;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long likescount =0L;
        private Long commentscount =0L;





}
