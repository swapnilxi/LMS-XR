package com.lms.lmsproject.LmsProject.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lms.lmsproject.LmsProject.entity.Admin;
import com.lms.lmsproject.LmsProject.entity.Role;
import com.lms.lmsproject.LmsProject.repository.AdminRepo;
import com.lms.lmsproject.LmsProject.utils.JwtUtils;

@Service
public class AdminService {

    @Autowired
    private AdminRepo adminRepoService;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    public String loginAdmin(Admin admin) {
        Optional<Admin> optionalUser = adminRepoService.findByAdminName(admin.getAdminName());

        if (optionalUser.isPresent()) {
            // Authenticate using the found user's credentials
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    admin.getAdminName(),
                    admin.getAdminPassword()));

            // Load UserDetails and generate JWT
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(admin.getAdminName());
            String jwt = jwtUtils.generateToken(userDetails);
            return jwt;

        } else {
            throw new UsernameNotFoundException("Incorrect username or password");
        }
    }

    public Admin getAuthenticatedAdmin() {
        // Retrieve the currently authenticated user's username
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();

        // Fetch the admin details based on the username from the database
        return adminRepoService.findByAdminName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User is not found!"));
    }

    public List<Admin> getAllAdmins() {
        return adminRepoService.findAll();
    }

    public Admin createNewAdmin(Admin reqAdmin) {

        if (reqAdmin.getAdminName() == null || reqAdmin.getAdminName().trim().isEmpty()) {
            throw new IllegalArgumentException("UserName can Not be Null");
        }
        if (reqAdmin.getAdminEmail() == null || reqAdmin.getAdminEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("User Email can Not be Null");
        }
        if (reqAdmin.getAdminPassword() == null || reqAdmin.getAdminPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("User Password can Not be Null");
        }

        // Check if the adminEmail is already registered
        Optional<Admin> existingAdmin = adminRepoService.findByAdminEmail(reqAdmin.getAdminEmail());
        if (existingAdmin.isPresent()) {
            throw new IllegalArgumentException("Admin with this email is already registered");
        }

        Admin newAdmin = Admin.builder()
                .adminId(UUID.randomUUID().toString())
                .adminEmail(reqAdmin.getAdminEmail())
                .adminName(reqAdmin.getAdminName())
                .adminPassword(passwordEncoder.encode(reqAdmin.getAdminPassword()))
                .roles(Set.of(Role.ADMIN))
                .build();

        return adminRepoService.save(newAdmin);
    }

    public Admin findAdminByEmail(String email) {
        if (adminRepoService.findByAdminEmail(email).isPresent()) {
            return adminRepoService.findByAdminEmail(email).get();
        } else {
            throw new UsernameNotFoundException("Admin with this Email Not Found !");
        }
    }

    @Transactional
    public Admin updateAdmin(Admin reqAdmin) {
        Admin authenticatedAdmin = getAuthenticatedAdmin(); // Get current admin
    
        if (authenticatedAdmin.getAdminId() == null) {
            throw new IllegalArgumentException("Authenticated Admin ID is null");
        }
    
        // Update only fields that are not null in reqAdmin
        if (reqAdmin.getAdminEmail() != null) {
            authenticatedAdmin.setAdminEmail(reqAdmin.getAdminEmail());
        }
        if (reqAdmin.getAdminName() != null) {
            authenticatedAdmin.setAdminName(reqAdmin.getAdminName());
        }
        if (reqAdmin.getAdminPassword() != null && !reqAdmin.getAdminPassword().isEmpty()) {
            authenticatedAdmin.setAdminPassword(passwordEncoder.encode(reqAdmin.getAdminPassword()));
        }
    
        return adminRepoService.save(authenticatedAdmin);
    }
    

    public void deleteAdminById() {

        Admin admin = adminRepoService.findById(getAuthenticatedAdmin().getAdminId())
                .orElseThrow(() -> new UsernameNotFoundException("Admin Not Found !"));

        // if (!admin.getAdminId().equals(getAuthenticatedAdmin().getAdminId())) {
        // throw new IllegalArgumentException("You are not authorized to delete this
        // Admin");
        // }
        adminRepoService.delete(admin);
    }

    public String findAdminByID() {

        Admin admin = adminRepoService.findById(getAuthenticatedAdmin().getAdminId())
                .orElseThrow(() -> new UsernameNotFoundException("Admin Not Found !"));

        return admin.getAdminId();
    }

}
