package com.lms.lmsproject.LmsProject.controllers;

import java.util.List;

// import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@RequestMapping(path = "/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserEntService userEntService;

    @Autowired
    private TeacherService teacherService;

    @GetMapping(path = "/all-admin")
    ResponseEntity<APIResponse<List<Admin>>> getAllAdmins() {
        try {
            List<Admin> admins = adminService.getAllAdmins();
            return new ResponseEntity<>(new APIResponse<List<Admin>>(HttpStatus.OK.value(), "Success", admins),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new APIResponse<List<Admin>>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error", null),
                    HttpStatus.OK);
        }
    }

    @PostMapping(path = "/create-admin")
    ResponseEntity<APIResponse<Admin>> createAdmin(@RequestBody Admin admin) {
        try {
            Admin savedAdmin = adminService.createNewAdmin(admin);
            return new ResponseEntity<>(new APIResponse<Admin>(HttpStatus.CREATED.value(), "Success", savedAdmin),
                    HttpStatus.OK);
        } catch (Exception IllegalArgumentException) {
            return new ResponseEntity<>(
                    new APIResponse<Admin>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            IllegalArgumentException.getMessage(), null),
                    HttpStatus.OK);
        }
    }

    @GetMapping(path = "/find-admin/{email}")
    ResponseEntity<APIResponse<Admin>> findAdmin(@PathVariable String email) {
        try {
            Admin admin = adminService.findAdminByEmail(email);
            return new ResponseEntity<>(new APIResponse<Admin>(HttpStatus.OK.value(), "Success", admin),
                    HttpStatus.OK);
        } catch (Exception UsernameNotFoundException) {
            return new ResponseEntity<>(
                    new APIResponse<Admin>(HttpStatus.NOT_FOUND.value(),
                            UsernameNotFoundException.getMessage(), null),
                    HttpStatus.OK);
        }
    }

    @GetMapping(path = "/find-admin-id")
    ResponseEntity<APIResponse<String>> findAdminById() {
        try {
            String admin = adminService.findAdminByID();
            return new ResponseEntity<>(new APIResponse<String>(HttpStatus.OK.value(), "Success", admin),
                    HttpStatus.OK);
        } catch (Exception UsernameNotFoundException) {
            return new ResponseEntity<>(
                    new APIResponse<String>(HttpStatus.NOT_FOUND.value(),
                            UsernameNotFoundException.getMessage(), null),
                    HttpStatus.OK);
        }
    }

    @PutMapping(path = "/update-admin")
    ResponseEntity<APIResponse<Admin>> updateAdmin(@RequestBody Admin admin) {
        try {
            Admin updatedAdmin = adminService.updateAdmin(admin);
            return new ResponseEntity<>(new APIResponse<Admin>(HttpStatus.OK.value(), "Success", updatedAdmin),
                    HttpStatus.OK);
        } catch (Exception IllegalArgumentException) {
            return new ResponseEntity<>(
                    new APIResponse<Admin>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            IllegalArgumentException.getMessage(), null),
                    HttpStatus.OK);
        }
    }

    @DeleteMapping(path = "/delete-admin")
    ResponseEntity<APIResponse<Void>> updateAdmin() {
        try {
            adminService.deleteAdminById();
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(), "Success", null),
                    HttpStatus.OK);
        } catch (Exception IllegalArgumentException) {
            return new ResponseEntity<>(
                    new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            IllegalArgumentException.getMessage(), null),
                    HttpStatus.OK);
        }
    }

    // User Controllers

    @GetMapping(path = "/all-user")
    public ResponseEntity<APIResponse<List<UserEnt>>> getAllUsers() {
        try {
            List<UserEnt> users = userEntService.getAllUsers();
            return new ResponseEntity<>(new APIResponse<List<UserEnt>>(HttpStatus.OK.value(), "Success", users),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new APIResponse<List<UserEnt>>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error", null),
                    HttpStatus.OK);
        }
    }

    @DeleteMapping(path = "/delete-user-id/{id}")
    public ResponseEntity<APIResponse<Void>> deleteUserByUserId(@PathVariable String id) {
        try {
            adminService.deleteUserById(id);
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.NO_CONTENT.value(), "Success", null),
                    HttpStatus.OK);
        } catch (Exception RuntimeException) {
            return new ResponseEntity<>(
                    new APIResponse<>(HttpStatus.NOT_FOUND.value(), RuntimeException.getMessage(), null),
                    HttpStatus.OK);
        }
    }

    @GetMapping(path = "/find-user-id/{id}")
    public ResponseEntity<APIResponse<UserEnt>> findUserById(@PathVariable String id) {
        try {
            UserEnt user = adminService.findUserById(id);
            return new ResponseEntity<>(new APIResponse<UserEnt>(HttpStatus.OK.value(), "Success", user),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new APIResponse<UserEnt>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error", null),
                    HttpStatus.OK);
        }
    }

    // Teacher Controllers

    @GetMapping(path = "/all-teacher")
    public ResponseEntity<APIResponse<List<Teacher>>> getAllTeachers() {
        try {
            List<Teacher> teachers = teacherService.fetchAllTeachers();
            return new ResponseEntity<>(new APIResponse<List<Teacher>>(HttpStatus.OK.value(), "Success", teachers),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new APIResponse<List<Teacher>>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error", null),
                    HttpStatus.OK);
        }
    }

    @DeleteMapping(path = "/delete-teacher-id/{id}")
    public ResponseEntity<APIResponse<Void>> deleteTeacherById(@PathVariable String id) {
        try {
            adminService.deleteTeacherById(id);
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.NO_CONTENT.value(), "Success", null),
                    HttpStatus.OK);
        } catch (Exception RuntimeException) {
            return new ResponseEntity<>(
                    new APIResponse<>(HttpStatus.NOT_FOUND.value(), RuntimeException.getMessage(), null),
                    HttpStatus.OK);
        }
    }

    @GetMapping(path = "/find-teacher-id/{id}")
    public ResponseEntity<APIResponse<Teacher>> findTeacherById(@PathVariable String id) {
        try {
            Teacher teacher = adminService.findTeacherById(id);
            return new ResponseEntity<>(new APIResponse<Teacher>(HttpStatus.OK.value(), "Success", teacher),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new APIResponse<Teacher>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error", null),
                    HttpStatus.OK);
        }
    }
}
