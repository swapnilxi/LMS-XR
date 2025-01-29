package com.lms.lmsproject.LmsProject.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lms.lmsproject.LmsProject.entity.Role;
import com.lms.lmsproject.LmsProject.entity.UserEnt;
import com.lms.lmsproject.LmsProject.repository.UserEntRepo;
import com.lms.lmsproject.LmsProject.utils.JwtUtils;

@Service
public class UserEntService {

    @Autowired
    private UserEntRepo userEntRepo;

    private UserEnt loggedInUser;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String loginUser(UserEnt user) {
        Optional<UserEnt> optionalUser = userEntRepo.findByUserName(user.getUserName());

        if (optionalUser.isPresent()) {
            // Authenticate using the found user's credentials
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUserName(),
                    user.getUserPassword()));

            // Load UserDetails and generate JWT
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(user.getUserName());
            String jwt = jwtUtils.generateToken(userDetails);
            return jwt;

        } else {
            throw new UsernameNotFoundException("Incorrect username or password");
        }
    }

    public UserEnt getAuthenticateUserEnt() {
        if (loggedInUser == null) {
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getUsername();
            loggedInUser = userEntRepo.findByUserName(username).get();
            if (loggedInUser == null) {
                throw new UsernameNotFoundException("User is Not Found !");
            }
        }
        return loggedInUser;
    }

    public List<UserEnt> getAllUsers() {
        return userEntRepo.findAll();
    }

    public UserEnt findUserByEmail(String email) {
        if (userEntRepo.findByUserEmail(email).isPresent()) {
            return userEntRepo.findByUserEmail(email).get();
        } else {
            throw new UsernameNotFoundException("User with this Email Not Found !");
        }
    }

    public UserEnt createNewUser(UserEnt requestUser) {
        if (requestUser.getFirstName() == null || requestUser.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First Name can Not be Null");
        }
        if (requestUser.getLastName() == null || requestUser.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last Name can Not be Null");
        }
        if (requestUser.getUserName() == null || requestUser.getUserName().trim().isEmpty()) {
            throw new IllegalArgumentException("UserName can Not be Null");
        }
        if (requestUser.getUserEmail() == null || requestUser.getUserEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("User Email can Not be Null");
        }
        if (requestUser.getUserPassword() == null || requestUser.getUserPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("User Password can Not be Null");
        }

        Optional<UserEnt> existingUserEmail = userEntRepo.findByUserEmail(requestUser.getUserEmail());
        Optional<UserEnt> existingUserName = userEntRepo.findByUserName(requestUser.getUserName());
        if (existingUserEmail.isPresent()) {
            throw new IllegalArgumentException("User with this email is already registered");
        }
        if (existingUserName.isPresent()) {
            throw new IllegalArgumentException("Username is already used");
        }

        UserEnt user = UserEnt.builder()
                .firstName(requestUser.getFirstName())
                .lastName(requestUser.getLastName())
                .userName(requestUser.getUserName())
                .userEmail(requestUser.getUserEmail())
                .userPassword(passwordEncoder.encode(requestUser.getUserPassword()))
                .roles(Set.of(Role.USER))
                .build();
        return userEntRepo.save(user);
    }

    public UserEnt updateUser(UserEnt reqUser) {

        UserEnt exestingUser = userEntRepo.findById(getAuthenticateUserEnt().getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User Id Not Valid"));

        if (reqUser.getFirstName() != null) {
            exestingUser.setFirstName(reqUser.getFirstName());
        }
        if (reqUser.getLastName() != null) {
            exestingUser.setLastName(reqUser.getLastName());
        }
        if (reqUser.getUserName() != null) {
            exestingUser.setUserName(reqUser.getUserName());
        }
        if (reqUser.getUserEmail() != null) {
            exestingUser.setUserEmail(reqUser.getUserEmail());
        }
        if (reqUser.getUserPassword() != null) {
            exestingUser.setUserPassword(passwordEncoder.encode(reqUser.getUserPassword()));
        }

        return userEntRepo.save(exestingUser);
    }

    public void deleteUser() {
        UserEnt user = userEntRepo.findById(getAuthenticateUserEnt().getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User Id Not Found!"));

        if (!user.getUserId().equals(getAuthenticateUserEnt().getUserId())) {
            throw new IllegalArgumentException("You can only delete your own profile");
        }
        userEntRepo.delete(user);
    }
}
