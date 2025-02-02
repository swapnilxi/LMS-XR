package com.lms.lmsproject.LmsProject.entity;


import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEnt {

    @Id
    private String userId;

    private String firstName;

    private String lastName;

    @Field("userName")
    private String userName;

    @Field("userEmail")
    private String userEmail;

    private String userPassword;

    
    private Set<Role> roles;
}
