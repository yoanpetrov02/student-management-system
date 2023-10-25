package com.yoanpetrov.studentmanagementsystem.rest.controllers;

import com.yoanpetrov.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.yoanpetrov.studentmanagementsystem.entities.Course;
import com.yoanpetrov.studentmanagementsystem.entities.User;
import com.yoanpetrov.studentmanagementsystem.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Rest controller for the courses endpoints.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private static final Logger LOG = LoggerFactory.getLogger(CourseController.class);

    private final CourseService courseService;

    /**
     * Gets all courses.
     *
     * @return 204 if there are no existing courses,
     * 200 and the course list if there is at least 1 course.
     */
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        LOG.debug("Getting all courses");
        List<Course> courses = new ArrayList<>(courseService.getAllCourses());
        if (courses.isEmpty()) {
            LOG.debug("No existing courses, returning 204");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    /**
     * Gets a course by its id.
     *
     * @param id the id of the course.
     * @return 404 if the course was not found,
     * 200 and the course if it was found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        LOG.debug("Getting course with id {}", id);
        try {
            Course course = courseService.getCourseById(id);
            return new ResponseEntity<>(course, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            LOG.debug("The course was not found, returning 404");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Gets all the users inside the course with the given id.
     *
     * @param id the id of the course.
     * @return 200 and a list of the users if everything is ok,
     * 204 if there are no users in the course,
     * 404 if the course was not found.
     */
    @GetMapping("/{id}/users")
    public ResponseEntity<List<User>> getAllCourseUsers(@PathVariable Long id) {
        LOG.debug("Getting all users in course with id {}", id);
        try {
            List<User> users = courseService.getAllCourseUsers(id);
            if (users.isEmpty()) {
                LOG.debug("No existing users in course, returning 204");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            LOG.debug("The course was not found, returning 404");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Creates the given {@code Course}.
     *
     * @param course the course to be created.
     * @return 200 and the created course if it was successfully created.
     */
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        LOG.debug("Creating new course");
        Course createdCourse = courseService.createCourse(course);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    /**
     * Adds a user to a course.
     *
     * @param courseId    the id of the course.
     * @param requestUser the {@code User} to be added to the course.
     * @return 201 with the user if the action was successful,
     * 404 if the user or the course weren't found.
     */
    @PostMapping("/{courseId}/users")
    public ResponseEntity<User> addUserToCourse(
        @PathVariable Long courseId,
        @RequestBody User requestUser
    ) {
        LOG.debug("Adding user to course with id {}", courseId);
        try {
            User user = courseService.addUserToCourse(courseId, requestUser);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            LOG.debug("The course or the user were not found, returning 404");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Updates the course with the given id with the new course details.
     *
     * @param id            the id of the existing course.
     * @param courseDetails the new details of the course.
     * @return 200 and the changed course if the action was successful,
     * 404 if the course was not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(
        @PathVariable Long id,
        @RequestBody Course courseDetails
    ) {
        LOG.debug("Updating course with id {}", id);
        try {
            Course course = courseService.updateCourse(id, courseDetails);
            return new ResponseEntity<>(course, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            LOG.debug("The course was not found, returning 404");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Deletes all existing courses.
     *
     * @return 200 and a message if the deletion was successful.
     */
    @DeleteMapping
    public ResponseEntity<String> deleteAllCourses() {
        LOG.debug("Deleting all courses");
        courseService.deleteAllCourses();
        return new ResponseEntity<>("All courses have been deleted successfully", HttpStatus.OK);
    }

    /**
     * Deletes a course by its course id.
     *
     * @param id the id of the course to be deleted.
     * @return 200 and a message if the deletion was successful,
     * 404 if the course was not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        LOG.debug("Deleting course with id {}", id);
        try {
            courseService.deleteCourse(id);
            return new ResponseEntity<>("Course deleted successfully", HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            LOG.debug("The course was not found, returning 404");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Removes a user from a course.
     *
     * @param courseId    the id of the course.
     * @param requestUser the {@code User} to be removed from the course.
     * @return 200 with the user if the action was successful,
     * 404 if the user or the course weren't found.
     */
    @DeleteMapping("/{courseId}/users")
    public ResponseEntity<User> removeUserFromCourse(
        @PathVariable Long courseId,
        @RequestBody User requestUser
    ) {
        LOG.debug("Removing user from course with id {}", courseId);
        try {
            User user = courseService.removeUserFromCourse(courseId, requestUser);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            LOG.debug("The course or the user were not found, returning 404");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
