package com.lms.lmsproject.LmsProject.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.lms.lmsproject.LmsProject.entity.Admin;

@Repository
public interface AdminRepo extends MongoRepository<Admin, String> {

    Optional<Admin> findByAdminEmail(String adminEmail);
    Optional<Admin> findByAdminName(String adminName);

}
