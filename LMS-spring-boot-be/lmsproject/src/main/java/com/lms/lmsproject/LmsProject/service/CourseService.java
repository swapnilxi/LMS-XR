package com.lms.lmsproject.LmsProject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lms.lmsproject.LmsProject.entity.Course;
import com.lms.lmsproject.LmsProject.entity.Teacher;
import com.lms.lmsproject.LmsProject.repository.CourseRepo;
import com.lms.lmsproject.LmsProject.repository.TeacherRepo;

@Service
public class CourseService {

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private TeacherRepo teacherRepo;

    private Teacher cachedTeacher;

    private Teacher getAuthenticatedTeacher() {
        if (cachedTeacher == null) {
            // Get the logged-in teacher's username
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getUsername();
            // Fetch the teacher from the database
            cachedTeacher = teacherRepo.findByTeacherUsername(username).get();
            if (cachedTeacher == null) {
                throw new UsernameNotFoundException("Teacher not found");
            }
        }
        return cachedTeacher;
    }

    public List<Course> getAllCourses() {
        return courseRepo.findAll();
    }

    public Course createNewCourse(Course course) {

        if (course.getCourseTitle() == null || course.getCourseTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Course Title can Not be Null");
        }
        if (course.getCourseDescription() == null || course.getCourseDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Course Description can Not be Null");
        }
        if (course.getCourseUrl() == null || course.getCourseUrl().trim().isEmpty()) {
            throw new IllegalArgumentException("Course URL can Not be Null");
        }
        if (course.getDuration() == null || course.getDuration().trim().isEmpty()) {
            throw new IllegalArgumentException("Course Duration can Not be Null");
        }

        Course newCourse = Course.builder()
                .courseTitle(course.getCourseTitle())
                .courseDescription(course.getCourseDescription())
                .teacher(getAuthenticatedTeacher())
                .teacherName(getAuthenticatedTeacher().getTeacherUsername())
                .courseUrl(course.getCourseUrl())
                .duration(course.getDuration())
                .build();

        return courseRepo.save(newCourse);
    }

    public List<Course> findCourseByTeacherName(String name) {

        if (teacherRepo.findByTeacherUsername(name).isPresent()) {
            return teacherRepo.findByTeacherUsername(name).get().getCourses();
        } else {
            throw new UsernameNotFoundException("Teacher Not Found !");
        }
    }

    public Course updateCourse(Course courseDetails) { // it requires course id as curz of multi courses
        // Fetch the course by ID

        Course existingCourse = courseRepo.findById(courseDetails.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        // Get the authenticated (logged-in) teacher
        Teacher authenticatedTeacher = getAuthenticatedTeacher();

        // Check if the course belongs to the logged-in teacher
        if (!existingCourse.getTeacher().getTeacherId().equals(authenticatedTeacher.getTeacherId())) {
            throw new IllegalArgumentException("You are not authorized to update this course");
        }

        // Update course details if the teacher owns the course
        if (courseDetails.getCourseTitle() != null) {
            existingCourse.setCourseTitle(courseDetails.getCourseTitle());
        }
        if (courseDetails.getCourseDescription() != null) {
            existingCourse.setCourseDescription(courseDetails.getCourseDescription());
        }
        if (courseDetails.getCourseUrl() != null) {
            existingCourse.setCourseUrl(courseDetails.getCourseUrl());
        }
        if (courseDetails.getDuration() != null) {
            existingCourse.setDuration(courseDetails.getDuration());
        }

        return courseRepo.save(existingCourse);
    }

    public void deleteCourse(String courseId) {
        // Get the course by ID
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        // Get the authenticated (logged-in) teacher
        Teacher authenticatedTeacher = getAuthenticatedTeacher();

        // Check if the authenticated teacher is the owner of the course
        if (!course.getTeacher().getTeacherId().equals(authenticatedTeacher.getTeacherId())) {
            throw new IllegalArgumentException("You are not authorized to delete this course");
        }

        // If the authenticated teacher is the owner, delete the course
        courseRepo.delete(course);
    }

}
