package com.lms.lmsproject.LmsProject.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    private String postId;

    @NonNull
    private String title;

    @NonNull
    private String content;

    private PostEnu catagories;

    @DBRef
    @JsonIgnore
    private Teacher teacher; // Reference to the Teacher entity

    private String teacherName;

}
