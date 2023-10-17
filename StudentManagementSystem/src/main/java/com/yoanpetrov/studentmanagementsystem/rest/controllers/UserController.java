package com.yoanpetrov.studentmanagementsystem.rest.controllers;

import com.yoanpetrov.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.yoanpetrov.studentmanagementsystem.entities.Course;
import com.yoanpetrov.studentmanagementsystem.entities.User;
import com.yoanpetrov.studentmanagementsystem.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<List<Course>> getAllUserCourses(@PathVariable Long id) {
        if (!userService.existsUserById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        List<Course> courses = userService.getAllUserCourses(id);
        if (courses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) { // TODO: 02-Oct-23 check if user already exists first
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/{userId}/courses")
    public ResponseEntity<Course> addCourseToUser(@PathVariable Long userId, @RequestBody Course requestCourse) {
        Course course = userService.addCourseToUser(userId, requestCourse);
        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        if (!userService.existsUserById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        User user = userService.updateUser(id, userDetails);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllUsers() {
        userService.deleteAllUsers();
        return new ResponseEntity<>("All users have been deleted successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (!userService.existsUserById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userService.deleteUser(id);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{id}/courses")
    public ResponseEntity<Course> removeCourseFromUser(@PathVariable Long id, @RequestBody Course requestCourse) {
        Course course = userService.removeCourseFromUser(id, requestCourse);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }
}
