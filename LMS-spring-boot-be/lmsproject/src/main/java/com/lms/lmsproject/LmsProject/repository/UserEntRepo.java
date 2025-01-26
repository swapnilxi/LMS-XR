package com.lms.lmsproject.LmsProject.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.lms.lmsproject.LmsProject.entity.UserEnt;

@Repository
public interface UserEntRepo extends MongoRepository<UserEnt, String> {

    Optional<UserEnt> findByUserEmail(String userEmail);

    Optional<UserEnt> findByUserName(String userName);

}
