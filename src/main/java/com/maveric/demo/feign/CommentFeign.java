package com.maveric.demo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "comment-service", fallbackFactory = HystrixFallBackFactory.class)
public interface CommentFeign {

    @GetMapping("/api/v1/posts/{postId}/comments/counts")
    Long getCommentsCounts(@PathVariable("postId") String postId);

}
