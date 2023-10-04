package com.yoanpetrov.studentmanagementsystem.controller;

import com.yoanpetrov.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.yoanpetrov.studentmanagementsystem.model.Course;
import com.yoanpetrov.studentmanagementsystem.model.User;
import com.yoanpetrov.studentmanagementsystem.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = new ArrayList<>(courseService.getAllCourses());
        if (courses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Course course = courseService.getCourseById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<User>> getAllCourseUsers(@PathVariable Long id) {
        if (!courseService.existsCourse(id)) {
            throw new ResourceNotFoundException("Course not found");
        }
        List<User> users = courseService.getAllCourseUsers(id);
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) { // TODO: 02-Oct-23 check if course already exists first
        Course createdCourse = courseService.createCourse(course);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    @PostMapping("/{courseId}/users")
    public ResponseEntity<User> addUserToCourse(@PathVariable Long courseId, @RequestBody User requestUser) {
        User user = courseService.addUserToCourse(courseId, requestUser);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        if (!courseService.existsCourse(id)) {
            throw new ResourceNotFoundException("Course not found");
        }
        Course course = courseService.updateCourse(id, courseDetails);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllCourses() {
        courseService.deleteAllCourses();
        return new ResponseEntity<>("All courses have been deleted successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        if (!courseService.existsCourse(id)) {
            throw new ResourceNotFoundException("Course not found");
        }
        courseService.deleteCourse(id);
        return new ResponseEntity<>("Course deleted successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{courseId}/users")
    public ResponseEntity<User> removeUserFromCourse(@PathVariable Long courseId, @RequestBody User requestUser) {
        User user = courseService.removeUserFromCourse(courseId, requestUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
