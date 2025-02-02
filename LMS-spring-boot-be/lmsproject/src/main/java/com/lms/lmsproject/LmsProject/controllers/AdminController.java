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
import com.lms.lmsproject.LmsProject.service.AdminService;

@RestController
@RequestMapping(path = "/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

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

}
