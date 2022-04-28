package com.maveric.demo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "like-service", fallbackFactory = HystrixFallBackFactory.class)

    public interface LikeFeign {
    @GetMapping("/api/v1/postsOrComments/{postorcommentId}/likes/counts")
     Long getLikesCounts(@PathVariable("postorcommentId") String postorcommentId);

    }

