package com.lms.lmsproject.LmsProject.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lms.lmsproject.LmsProject.repository.AdminRepo;
import com.lms.lmsproject.LmsProject.repository.TeacherRepo;
import com.lms.lmsproject.LmsProject.repository.UserEntRepo;
import com.lms.lmsproject.LmsProject.entity.Admin;
import com.lms.lmsproject.LmsProject.entity.Teacher;
import com.lms.lmsproject.LmsProject.entity.UserEnt;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserEntRepo userRepo;

    @Autowired
    private TeacherRepo teacherRepo;

    @Autowired
    private AdminRepo adminRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from the database
        Optional<UserEnt> userOpt = userRepo.findByUserName(username);
        if (userOpt.isPresent()) {
            return buildUserDetails(userOpt.get());
        }

        // If no user is found, try to find the teacher
        Optional<Teacher> teacherOpt = teacherRepo.findByTeacherUsername(username);
        if (teacherOpt.isPresent()) {
            return buildUserDetails(teacherOpt.get());
        }

        Optional<Admin> adminOpt = adminRepo.findByAdminName(username);
        if (adminOpt.isPresent()) {
            return buildUserDetails(adminOpt.get());
        }

        // If neither found, throw exception
        throw new UsernameNotFoundException("User not found");
    }

    private UserDetails buildUserDetails(UserEnt user) {
        String[] roles = user.getRoles().stream()
                .map(role -> role.name())
                .toArray(String[]::new);

        return User.builder()
                .username(user.getUserName())
                .password(user.getUserPassword())
                .roles(roles)
                .build();
    }

    private UserDetails buildUserDetails(Teacher teacher) {
        String[] roles = teacher.getRoles().stream()
                .map(role -> role.name())
                .toArray(String[]::new);

        return User.builder()
                .username(teacher.getTeacherUsername())
                .password(teacher.getTeacherPassword())
                .roles(roles)
                .build();
    }

    private UserDetails buildUserDetails(Admin admin) {
        String[] roles = admin.getRoles().stream()
                .map(role -> role.name())
                .toArray(String[]::new);

        return User.builder()
                .username(admin.getAdminName())
                .password(admin.getAdminPassword())
                .roles(roles)
                .build();
    }
}
