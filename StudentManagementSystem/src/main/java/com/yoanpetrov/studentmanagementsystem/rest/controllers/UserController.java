package com.yoanpetrov.studentmanagementsystem.rest.controllers;

import com.yoanpetrov.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.yoanpetrov.studentmanagementsystem.entities.Course;
import com.yoanpetrov.studentmanagementsystem.entities.User;
import com.yoanpetrov.studentmanagementsystem.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest controller for the users endpoints.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    /**
     * Gets all users.
     *
     * @return 204 if there are no existing users,
     * 200 and the user list if there is at least 1 user.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        LOG.debug("Getting all users");
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            LOG.debug("No existing users, returning 204");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Gets a user by its id.
     *
     * @param id the id of the user.
     * @return 404 if the user was not found,
     * 200 and the user if it was found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        LOG.debug("Getting user with id {}", id);
        try {
            User user = userService.getUserById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            LOG.debug("The user was not found, returning 404");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Gets all the courses that the user with the given id has enrolled in.
     *
     * @param id the id of the user.
     * @return 200 and a list of the courses if everything is ok,
     * 204 if the user is not enrolled in any courses,
     * 404 if the user was not found.
     */
    @GetMapping("/{id}/courses")
    public ResponseEntity<List<Course>> getAllUserCourses(@PathVariable Long id) {
        LOG.debug("Getting all courses of user with id {}", id);
        try {
            List<Course> courses = userService.getAllUserCourses(id);
            if (courses.isEmpty()) {
                LOG.debug("No existing courses in user, returning 204");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(courses, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            LOG.debug("The user was not found, returning 404");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Creates the given {@code User}.
     *
     * @param user the user to be created.
     * @return 200 and the created user if it was successfully created.
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        LOG.debug("Creating new user");
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Adds a course to a user.
     *
     * @param userId        the id of the user.
     * @param requestCourse the {@code Course} to be added to the user.
     * @return 201 with the course if the action was successful,
     * 404 if the user or the course weren't found.
     */
    @PostMapping("/{userId}/courses")
    public ResponseEntity<Course> addCourseToUser(
        @PathVariable Long userId,
        @RequestBody Course requestCourse
    ) {
        LOG.debug("Adding course to user with id {}", userId);
        try {
            Course course = userService.addCourseToUser(userId, requestCourse);
            return new ResponseEntity<>(course, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            LOG.debug("The course or the user were not found, returning 404");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates the user with the given id with the new user details.
     *
     * @param id          the id of the existing user.
     * @param userDetails the new details of the user.
     * @return 200 and the changed user if the action was successful,
     * 404 if the user was not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
        @PathVariable Long id,
        @RequestBody User userDetails
    ) {
        LOG.debug("Updating user with id {}", id);
        try {
            User user = userService.updateUser(id, userDetails);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            LOG.debug("The user was not found, returning 404");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Deletes all existing users.
     *
     * @return 200 and a message if the deletion was successful.
     */
    @DeleteMapping
    public ResponseEntity<String> deleteAllUsers() {
        LOG.debug("Deleting all users");
        userService.deleteAllUsers();
        return new ResponseEntity<>("All users have been deleted successfully", HttpStatus.OK);
    }

    /**
     * Deletes a user by its user id.
     *
     * @param id the id of the user to be deleted.
     * @return 200 and a message if the deletion was successful,
     * 404 if the user was not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        LOG.debug("Deleting user with id {}", id);
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            LOG.debug("The user was not found, returning 404");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Removes a course from a user.
     *
     * @param userId        the id of the user.
     * @param requestCourse the {@code Course} to be removed from the user.
     * @return 201 with the course if the action was successful,
     * 404 if the user or the course weren't found.
     */
    @DeleteMapping("/{userId}/courses")
    public ResponseEntity<Course> removeCourseFromUser(
        @PathVariable Long userId,
        @RequestBody Course requestCourse
    ) {
        LOG.debug("Removing course from user with id {}", userId);
        try {
            Course course = userService.removeCourseFromUser(userId, requestCourse);
            return new ResponseEntity<>(course, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            LOG.debug("The course or the user were not found, returning 404");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
