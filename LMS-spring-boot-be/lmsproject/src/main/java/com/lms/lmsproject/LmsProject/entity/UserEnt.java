package com.lms.lmsproject.LmsProject.entity;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.annotation.Nonnull;
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

    @Nonnull
    private String firstName;

    @Nonnull
    private String lastName;

    @Field("userName")
    @Indexed(unique = true)
    @Nonnull
    private String userName;

    @Field("userEmail")
    @Nonnull
    private String userEmail;

    @Nonnull
    private String userPassword;

    private Set<Role> roles;
}
