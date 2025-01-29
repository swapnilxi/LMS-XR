package com.lms.lmsproject.LmsProject.controllers;

import java.util.List;

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
import com.lms.lmsproject.LmsProject.entity.UserEnt;
import com.lms.lmsproject.LmsProject.service.UserEntService;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private UserEntService userEntService;

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

    @PostMapping(path = "/create-user")
    public ResponseEntity<APIResponse<UserEnt>> createNewUser(@RequestBody UserEnt user) {
        try {
            UserEnt newUser = userEntService.createNewUser(user);
            return new ResponseEntity<>(new APIResponse<UserEnt>(HttpStatus.CREATED.value(), "Success", newUser),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new APIResponse<UserEnt>(HttpStatus.CONFLICT.value(), e.getMessage(), null),
                    HttpStatus.OK);
        }

    }

    @GetMapping(path = "/find-user/{email}")
    public ResponseEntity<APIResponse<UserEnt>> findByEmail(@PathVariable String email) {
        try {
            UserEnt user = userEntService.findUserByEmail(email);
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(), "Success", user), HttpStatus.OK);
        } catch (Exception UsernameNotFoundException) {
            return new ResponseEntity<>(
                    new APIResponse<>(HttpStatus.NOT_FOUND.value(), UsernameNotFoundException.getMessage(), null),
                    HttpStatus.OK);
        }
    }

    @PutMapping(path = "/update-user")
    public ResponseEntity<APIResponse<UserEnt>> updateUserDetails(@RequestBody UserEnt user) {
        try {
            UserEnt updatedUser = userEntService.updateUser(user);
            return new ResponseEntity<>(new APIResponse<UserEnt>(HttpStatus.OK.value(), "Success", updatedUser),
                    HttpStatus.OK);
        } catch (Exception RuntimeException) {
            return new ResponseEntity<>(
                    new APIResponse<UserEnt>(HttpStatus.INTERNAL_SERVER_ERROR.value(), RuntimeException.getMessage(),
                            null),
                    HttpStatus.OK);
        }
    }

    @DeleteMapping(path = "/delete-user")
    public ResponseEntity<APIResponse<Void>> deleteUserByUserId() {
        try {
            userEntService.deleteUser();
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.NO_CONTENT.value(), "Success", null),
                    HttpStatus.OK);
        } catch (Exception RuntimeException) {
            return new ResponseEntity<>(
                    new APIResponse<>(HttpStatus.NOT_FOUND.value(), RuntimeException.getMessage(), null),
                    HttpStatus.OK);
        }
    }

}
