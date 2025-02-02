package com.lms.lmsproject.LmsProject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.lmsproject.LmsProject.customApiResponse.APIResponse;
import com.lms.lmsproject.LmsProject.entity.Admin;
import com.lms.lmsproject.LmsProject.entity.Teacher;
import com.lms.lmsproject.LmsProject.entity.UserEnt;
import com.lms.lmsproject.LmsProject.service.AdminService;
import com.lms.lmsproject.LmsProject.service.TeacherService;
import com.lms.lmsproject.LmsProject.service.UserEntService;

@RestController
@RequestMapping(path = "/public")
public class PublicController {

    @Autowired
    private UserEntService userEntService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private AdminService adminService;

    @PostMapping("/login-user")
    ResponseEntity<APIResponse<String>> loginUser(@RequestBody UserEnt user) {
        try {
            String token = userEntService.loginUser(user);
            return new ResponseEntity<>(new APIResponse<String>(HttpStatus.ACCEPTED.value(), "Success", token),
                    HttpStatus.OK);

        } catch (Exception UsernameNotFoundException) {
            return new ResponseEntity<>(new APIResponse<String>(HttpStatus.UNAUTHORIZED.value(),
                    UsernameNotFoundException.getMessage(), null), HttpStatus.OK);
        }
    }

    // teachers section

    @PostMapping(path = "/login-teacher")
    ResponseEntity<APIResponse<String>> loginTeacher(@RequestBody Teacher teacher) {
        try {
            String token = teacherService.loginTeacher(teacher);
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.ACCEPTED.value(), "Success", token),
                    HttpStatus.OK);

        } catch (Exception UsernameNotFoundException) {
            return new ResponseEntity<>(
                    new APIResponse<>(HttpStatus.UNAUTHORIZED.value(), UsernameNotFoundException.getMessage(), null),
                    HttpStatus.OK);
        }
    }

    // Admin Section

    @PostMapping(path = "/login-admin")
    ResponseEntity<APIResponse<String>> loginAdmin(@RequestBody Admin admin) {
        try {
            String token = adminService.loginAdmin(admin);
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.ACCEPTED.value(), "Success", token),
                    HttpStatus.OK);

        } catch (Exception UsernameNotFoundException) {
            return new ResponseEntity<>(
                    new APIResponse<>(HttpStatus.UNAUTHORIZED.value(), UsernameNotFoundException.getMessage(), null),
                    HttpStatus.OK);
        }
    }

}
