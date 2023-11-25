package com.yoanpetrov.studentmanagementsystem.controllers;

import com.yoanpetrov.studentmanagementsystem.entities.Course;
import com.yoanpetrov.studentmanagementsystem.entities.User;
import com.yoanpetrov.studentmanagementsystem.mappers.UserMapper;
import com.yoanpetrov.studentmanagementsystem.dto.UserDto;
import com.yoanpetrov.studentmanagementsystem.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final UserMapper userMapper;

    /**
     * Gets all users.
     *
     * @return 204 if there are no existing users,
     * 200 and the user list if there is at least 1 user.
     */
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        LOG.debug("Getting all users");
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            LOG.debug("No existing users, returning 204");
            return new ResponseEntity<>("No existing users", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Gets a user by its id.
     *
     * @param id the id of the user.
     * @return 200 and the user if it was found,
     * 404 if the user was not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        LOG.debug("Getting user with id {}", id);
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
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
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER') or @authenticationCheckerService.doUserIdsMatch(#id)")
    public ResponseEntity<?> getAllUserCourses(@PathVariable Long id) {
        LOG.debug("Getting all courses of user with id {}", id);
        List<Course> courses = userService.getAllUserCourses(id);
        if (courses.isEmpty()) {
            LOG.debug("No existing courses in user, returning 204");
            return new ResponseEntity<>("No existing courses", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    /**
     * Creates the given {@code User}.
     *
     * @param userDto the user to be created.
     * @return 200 and the created user if it was successfully created.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDto userDto) {
        LOG.debug("Creating new user");
        User createdUser = userService.createUser(
            userMapper.convertDtoToEntity(userDto));
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Adds a course to a user.
     *
     * @param userId   the id of the user.
     * @param courseId the id of the {@code Course} to be added to the user.
     * @return 201 with the course if the action was successful,
     * 404 if the user or the course weren't found.
     */
    @PostMapping("/{userId}/courses/{courseId}")
    @PreAuthorize("hasRole('ADMIN') " +
        "or (hasRole('TEACHER') and @authenticationCheckerService.isUserEnrolledInCourse(#courseId)) " +
        "or @authenticationCheckerService.doUserIdsMatch(#userId)"
    )
    public ResponseEntity<Course> addCourseToUser(
        @PathVariable Long userId,
        @PathVariable Long courseId
    ) {
        LOG.debug("Adding course to user with id {}", userId);
        Course course = userService.addCourseToUser(userId, courseId);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    /**
     * Updates the user with the given id with the new user details.
     *
     * @param id             the id of the existing user.
     * @param userDetailsDto the new details of the user.
     * @return 200 and the changed user if the action was successful,
     * 404 if the user was not found.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @authenticationCheckerService.doUserIdsMatch(#id)")
    public ResponseEntity<User> updateUser(
        @PathVariable Long id,
        @Valid @RequestBody UserDto userDetailsDto
    ) {
        LOG.debug("Updating user with id {}", id);
        User user = userService.updateUser(
            id, userMapper.convertDtoToEntity(userDetailsDto));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Deletes all existing users.
     *
     * @return 200 and a message if the deletion was successful.
     */
    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAllUsers() {
        LOG.debug("Deleting all users");
        userService.deleteAllUsers();
        return new ResponseEntity<>("All users have been deleted successfully", HttpStatus.OK);
    }

    /**
     * Deletes a user by its id.
     *
     * @param id the id of the user to be deleted.
     * @return 200 and a message if the deletion was successful,
     * 404 if the user was not found.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        LOG.debug("Deleting user with id {}", id);
        userService.deleteUser(id);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    /**
     * Removes a course from a user.
     *
     * @param userId   the id of the user.
     * @param courseId the id of the {@code Course} to be removed from the user.
     * @return 201 with the course if the action was successful,
     * 404 if the user or the course weren't found.
     */
    @DeleteMapping("/{userId}/courses/{courseId}")
    @PreAuthorize("hasRole('ADMIN') " +
        "or (hasRole('TEACHER') and @authenticationCheckerService.isUserEnrolledInCourse(#courseId)) " +
        "or @authenticationCheckerService.doUserIdsMatch(#userId)"
    )
    public ResponseEntity<Course> removeCourseFromUser(
        @PathVariable Long userId,
        @PathVariable Long courseId
    ) {
        LOG.debug("Removing course from user with id {}", userId);
        Course course = userService.removeCourseFromUser(userId, courseId);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }
}
