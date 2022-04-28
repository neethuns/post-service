package com.maveric.demo.repo;

import com.maveric.demo.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepo extends MongoRepository<Post, String> {


//    List<Post> findBypostedBy(String postedBy);

}
