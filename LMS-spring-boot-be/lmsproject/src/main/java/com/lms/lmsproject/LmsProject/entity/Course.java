package com.lms.lmsproject.LmsProject.entity;

// import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.annotation.Nonnull;
// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    private String courseId;

    @Nonnull
    private String courseTitle;

      @Field("courseDescription")
    @Nonnull
    private String courseDescription;

    @DBRef
    @JsonIgnore
    private Teacher teacher; // Reference to the teacher who created or manages the course

    private String teacherName;

    @Nonnull
    private String  courseUrl;

    // sub sections to add
    @Nonnull
    private String duration; // Duration in hours or minutes

}
