package com.lms.lmsproject.LmsProject.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.bson.types.ObjectId;
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
            UserEnt user = userEntRepo.findById(getAuthenticateUserEnt().getUserId()).orElseThrow(()-> new UsernameNotFoundException("User Id Not Valid"));
            user.setFirstName(reqUser.getFirstName());
            user.setLastName(reqUser.getLastName());
            user.setUserName(reqUser.getUserName());
            user.setUserEmail(reqUser.getUserEmail());
            if(reqUser.getUserPassword() != null){
                user.setUserPassword(passwordEncoder.encode(reqUser.getUserPassword()));
            }else{
            }
            user.setRoles(Set.of(Role.USER));
        // UserEnt updatedUser = UserEnt.builder()
        //         .userId(getAuthenticateUserEnt().getUserId())
        //         .firstName(reqUser.getFirstName())
        //         .lastName(reqUser.getLastName())
        //         .userName(reqUser.getUserName())
        //         .userEmail(reqUser.getUserEmail())
        //         .userPassword(passwordEncoder.encode(reqUser.getUserPassword()))
        //         .roles(Set.of(Role.USER))
        //         .build();
        return userEntRepo.save(user);

    }

    public void deleteUser(ObjectId id) {
        UserEnt user = userEntRepo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User Id Not Found!"));

        // if (!user.getUserId().equals(getAuthenticateUserEnt().getUserId())) {
        // throw new RuntimeException("You are not authorized to delete this user");
        // }
        userEntRepo.delete(user);
    }
}
