package com.maveric.demo.feign;

import com.maveric.demo.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "user-service",fallbackFactory = HystrixFallBackFactory.class)
public interface UserFeign {


    @GetMapping("/api/v1/users/{userId}")
    UserDto getUserDetails(@PathVariable("userId") String userId);
}

