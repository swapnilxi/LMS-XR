package com.lms.lmsproject.LmsProject.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.lms.lmsproject.LmsProject.entity.Teacher;

@Repository
public interface TeacherRepo extends MongoRepository<Teacher, String> {

    Optional<Teacher> findByTeacherEmail(String teacherEmail);

    Optional<Teacher> findByTeacherUsername(String teacherUsername);
}
