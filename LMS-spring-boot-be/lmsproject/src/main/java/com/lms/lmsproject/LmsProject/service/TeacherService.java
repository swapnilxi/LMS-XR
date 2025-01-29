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

import com.lms.lmsproject.LmsProject.entity.Role;
import com.lms.lmsproject.LmsProject.entity.Teacher;
import com.lms.lmsproject.LmsProject.repository.TeacherRepo;
import com.lms.lmsproject.LmsProject.utils.JwtUtils;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepo teacherRepo;

    private Teacher cachedTeacher;

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
        if (cachedTeacher == null) {
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getUsername();
            cachedTeacher = teacherRepo.findByTeacherUsername(username).get();
            if (cachedTeacher == null) {
                throw new UsernameNotFoundException("User Not Found");
            }
        }
        return cachedTeacher;
    }

    public List<Teacher> fetchAllTeachers() {
        return teacherRepo.findAll();
    }

    @Transactional
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

        Optional<Teacher> existingTeacherEmail = teacherRepo.findByTeacherEmail(reqTeacher.getTeacherEmail());
        Optional<Teacher> existingTeacherUserName = teacherRepo.findByTeacherUsername(reqTeacher.getTeacherUsername());
        if (existingTeacherEmail.isPresent()) {
            throw new IllegalArgumentException("Teacher with this email is already registered");
        }
        if (existingTeacherUserName.isPresent()) {
            throw new IllegalArgumentException("Teacher Username is already used");
        }

        Teacher newTeacher = Teacher.builder()
        .teacherId(UUID.randomUUID().toString())
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
        Teacher exestingTeacher = getAuthenticatedTeacher();

        if (reqTeacher.getTeacherUsername() != null) {
            exestingTeacher.setTeacherUsername(reqTeacher.getTeacherUsername());
        }
        if (reqTeacher.getTeacherEmail() != null) {
            exestingTeacher.setTeacherEmail(reqTeacher.getTeacherEmail());
        }

        if (reqTeacher.getTeacherPassword() != null) {
            exestingTeacher.setTeacherPassword(passwordEncoder.encode(reqTeacher.getTeacherPassword()));
        }

        if (reqTeacher.getExpertise() != null) {
            exestingTeacher.setExpertise(reqTeacher.getExpertise());
        }

        // Save the updated teacher
        Teacher saveTeacher = teacherRepo.save(exestingTeacher);

        // Invalidate the cached loggedInTeacher
        cachedTeacher = null;

        return saveTeacher;
    }

    public void deleteTeacher() {
        Teacher teacher = teacherRepo.findById(getAuthenticatedTeacher().getTeacherId())
                .orElseThrow(() -> new UsernameNotFoundException("Teacher ID does not exist"));

        if (!teacher.getTeacherId().equals(getAuthenticatedTeacher().getTeacherId())) {
            throw new IllegalArgumentException("You can only delete your own profile");
        }

        teacherRepo.delete(teacher);
    }

}
