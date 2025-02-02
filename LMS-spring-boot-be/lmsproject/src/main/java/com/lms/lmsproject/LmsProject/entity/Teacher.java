package com.lms.lmsproject.LmsProject.entity;

import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.annotation.Nonnull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Teacher {

    @Id
    private String teacherId;

    @Field("teacherUsername")
    @Nonnull
    private String teacherUsername;

    @Field("teacherEmail")
    @Nonnull
    private String teacherEmail;

    @Nonnull
    private String teacherPassword;

    @DBRef
    private List<Post> posts;

    @Nonnull
    private String expertise; // Area of expertise, e.g., "Java", "Machine Learning"

    private Set<Role> roles;

    @DBRef
    private List<Course> courses;

}
