package com.lms.lmsproject.LmsProject.entity;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Admin {

    @Id
    private String adminId;

    @Field("adminEmail")
    private String adminEmail;

    @Field("adminName")
    private String adminName;

    @Nonnull
    private String adminPassword;

    private Set<Role> roles;
}
