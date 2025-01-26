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
import com.lms.lmsproject.LmsProject.entity.Teacher;
import com.lms.lmsproject.LmsProject.repository.TeacherRepo;
import com.lms.lmsproject.LmsProject.utils.JwtUtils;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepo teacherRepo;

    private Teacher loggedInTeacher;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    public String loginTeacher(Teacher teacher) {
        // Fetch the teacher by username using Optional
        Optional<Teacher> optionalTeacher = teacherRepo.findByTeacherUsername(teacher.getTeacherUsername());

        if (optionalTeacher.isPresent()) {
            // Authenticate using the found teacher's credentials
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    teacher.getTeacherUsername(),
                    teacher.getTeacherPassword()));

            // Load UserDetails and generate JWT
            UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(teacher.getTeacherUsername());
            String jwt = jwtUtils.generateToken(userDetails);
            return jwt;

        } else {
            throw new UsernameNotFoundException("Incorrect username or password");
        }
    }

    public Teacher getAuthenticatedTeacher() {
        if (loggedInTeacher == null) {
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getUsername();
            loggedInTeacher = teacherRepo.findByTeacherUsername(username).get();
            if (loggedInTeacher == null) {
                throw new UsernameNotFoundException("User Not Found");
            }
        }
        return loggedInTeacher;
    }

    public List<Teacher> fetchAllTeachers() {
        return teacherRepo.findAll();
    }

    public Teacher createNewTeacher(Teacher reqTeacher) {

        if (reqTeacher.getTeacherUsername() == null || reqTeacher.getTeacherUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Teacher Username cannot be null");
        }
        if (reqTeacher.getTeacherEmail() == null || reqTeacher.getTeacherEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Teacher Email cannot be null");
        }
        if (reqTeacher.getTeacherPassword() == null || reqTeacher.getTeacherPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Teacher Password cannot be null");
        }
        if (reqTeacher.getExpertise() == null || reqTeacher.getExpertise().trim().isEmpty()) {
            throw new IllegalArgumentException("Teacher Expertise cannot be null");
        }

        Teacher newTeacher = Teacher.builder()
                .teacherUsername(reqTeacher.getTeacherUsername())
                .teacherEmail(reqTeacher.getTeacherEmail())
                .teacherPassword(passwordEncoder.encode(reqTeacher.getTeacherPassword())) // Avoid double setting
                .expertise(reqTeacher.getExpertise())
                .roles(Set.of(Role.TEACHER))
                .build();

        return teacherRepo.save(newTeacher);
    }

    public Teacher findByTeacherEmail(String email) {
        if (teacherRepo.findByTeacherEmail(email).isPresent()) {
            return teacherRepo.findByTeacherEmail(email).get();
        } else {
            throw new UsernameNotFoundException("Teacher with this Email Not Found !");
        }
    }

    public Teacher updateTeacher(Teacher reqTeacher) {

        // Get the current authenticated teacher
        Teacher currentTeacher = getAuthenticatedTeacher();

        // Build the updated teacher object
        Teacher updatedTeacher = Teacher.builder()
                .teacherId(currentTeacher.getTeacherId()) // Keep the current teacher ID
                .teacherUsername(reqTeacher.getTeacherUsername())
                .teacherEmail(reqTeacher.getTeacherEmail())
                .teacherPassword(passwordEncoder.encode(reqTeacher.getTeacherPassword())) // Re-encode the password
                .expertise(reqTeacher.getExpertise())
                .roles(Set.of(Role.TEACHER)) // Assuming the role stays the same
                .build();

        // Save the updated teacher
        Teacher savedTeacher = teacherRepo.save(updatedTeacher);

        // Invalidate the cached loggedInTeacher
        loggedInTeacher = null;

        return savedTeacher;
    }

    public void deleteTeacher(String id) {
        Teacher teacher = teacherRepo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Teacher with ID " + id + " does not exist"));

        // if (!teacher.getTeacherId().equals(getAuthenticatedTeacher().getTeacherId()))
        // {
        // throw new IllegalArgumentException("You can only delete your own profile");
        // }

        teacherRepo.delete(teacher);
    }

}
