package com.lms.lmsproject.LmsProject.entity;


import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

// import jakarta.annotation.Nonnull;
// import jakarta.persistence.Column;
// import jakarta.persistence.ElementCollection;
// import jakarta.persistence.Entity;
// import jakarta.persistence.EnumType;
// import jakarta.persistence.Enumerated;
// import jakarta.persistence.FetchType;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// add guest user
@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEnt {

    @Id
    private ObjectId userId;

    private String firstName;

    private String lastName;

    @Field("userName")
    private String userName;

    @Field("userEmail")
    private String userEmail;

    private String userPassword;

    
    private Set<Role> roles;
}
