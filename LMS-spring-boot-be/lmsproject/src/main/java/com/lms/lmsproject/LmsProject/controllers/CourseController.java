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
import com.lms.lmsproject.LmsProject.entity.Course;
import com.lms.lmsproject.LmsProject.service.CourseService;

@RestController
@RequestMapping(path = "/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping(path = "/all-course")
    public ResponseEntity<APIResponse<List<Course>>> fetchAllCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();
            return new ResponseEntity<>(new APIResponse<List<Course>>(HttpStatus.OK.value(), "Success", courses),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new APIResponse<List<Course>>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error", null),
                    HttpStatus.OK);
        }
    }

    @PostMapping(path = "/create-course")
    public ResponseEntity<APIResponse<Course>> createCourse(@RequestBody Course course) {
        try {
            Course newCourse = courseService.createNewCourse(course);
            return new ResponseEntity<>(new APIResponse<Course>(HttpStatus.CREATED.value(), "Success", newCourse),
                    HttpStatus.OK);
        } catch (Exception IllegalArgumentException) {
            return new ResponseEntity<>(
                    new APIResponse<Course>(HttpStatus.CONFLICT.value(), IllegalArgumentException.getMessage(), null),
                    HttpStatus.OK);
        }
    }

    @GetMapping(path = "/find-course/{teachername}")
    public ResponseEntity<APIResponse<List<Course>>> findCourseByTeacherName(@PathVariable String teachername) {
        try {
            List<Course> coruses = courseService.findCourseByTeacherName(teachername);
            return new ResponseEntity<>(new APIResponse<List<Course>>(HttpStatus.OK.value(), "Success", coruses),
                    HttpStatus.OK);
        } catch (Exception UsernameNotFoundException) {
            return new ResponseEntity<>(
                    new APIResponse<List<Course>>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            UsernameNotFoundException.getMessage(), null),
                    HttpStatus.OK);
        }
    }

    @PutMapping(path = "/update-course")
    public ResponseEntity<APIResponse<Course>> updateCourse(@RequestBody Course course) {
        try {
            Course updatedCourse = courseService.updateCourse(course);
            return new ResponseEntity<>(new APIResponse<Course>(HttpStatus.CREATED.value(), "Success", updatedCourse),
                    HttpStatus.OK);
        } catch (Exception IllegalArgumentException) {
            return new ResponseEntity<>(
                    new APIResponse<Course>(HttpStatus.CONFLICT.value(), IllegalArgumentException.getMessage(), null),
                    HttpStatus.OK);
        }
    }

    @DeleteMapping(path = "/delete-course/{id}")
    public ResponseEntity<APIResponse<Void>> deleteCourse(@PathVariable String id) {
        try {
            courseService.deleteCourse(id);
            return new ResponseEntity<>(new APIResponse<>(HttpStatus.OK.value(), "Success", null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new APIResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null),
                    HttpStatus.OK);
        }
    }

}
