package com.lms.lmsproject.LmsProject.repository;
// import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.lms.lmsproject.LmsProject.entity.Post;
public interface PostRepo extends MongoRepository<Post, String> {

    
}
