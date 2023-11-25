package com.yoanpetrov.studentmanagementsystem.controllers;

import com.yoanpetrov.studentmanagementsystem.entities.Course;
import com.yoanpetrov.studentmanagementsystem.entities.User;
import com.yoanpetrov.studentmanagementsystem.mappers.CourseMapper;
import com.yoanpetrov.studentmanagementsystem.dto.CourseDto;
import com.yoanpetrov.studentmanagementsystem.services.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final CourseMapper courseMapper;

    /**
     * Gets all courses.
     *
     * @return 204 if there are no existing courses,
     * 200 and the course list if there is at least 1 course.
     */
    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        LOG.debug("Getting all courses");
        List<Course> courses = new ArrayList<>(courseService.getAllCourses());
        if (courses.isEmpty()) {
            LOG.debug("No existing courses, returning 204");
            return new ResponseEntity<>("No existing courses", HttpStatus.NO_CONTENT);
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
    @PreAuthorize(
        "hasAnyRole('ADMIN', 'TEACHER') " +
        "or @authenticationCheckerService.isUserEnrolledInCourse(#id)"
    )
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        LOG.debug("Getting course with id {}", id);
        Course course = courseService.getCourseById(id);
        return new ResponseEntity<>(course, HttpStatus.OK);
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
    @PreAuthorize(
        "hasAnyRole('ADMIN', 'TEACHER') " +
        "or @authenticationCheckerService.isUserEnrolledInCourse(#id)"
    )
    public ResponseEntity<?> getAllCourseUsers(@PathVariable Long id) {
        LOG.debug("Getting all users in course with id {}", id);
        List<User> users = courseService.getAllCourseUsers(id);
        if (users.isEmpty()) {
            LOG.debug("No existing users in course, returning 204");
            return new ResponseEntity<>("No existing users in course", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Creates the given {@code Course}.
     *
     * @param courseDto the course to be created.
     * @return 200 and the created course if it was successfully created.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Course> createCourse(@Valid @RequestBody CourseDto courseDto) {
        LOG.debug("Creating new course");
        Course createdCourse = courseService.createCourse(
            courseMapper.convertDtoToEntity(courseDto));
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    /**
     * Adds a user to a course.
     *
     * @param courseId the id of the course.
     * @param userId   the id of the {@code User} to be added to the course.
     * @return 201 with the user if the action was successful,
     * 404 if the user or the course weren't found.
     */
    @PostMapping("/{courseId}/users/{userId}")
    @PreAuthorize(
        "hasRole('ADMIN') " +
        "or (hasRole('TEACHER') and @authenticationCheckerService.isUserEnrolledInCourse(#courseId)) " +
        "or @authenticationCheckerService.doUserIdsMatch(#userId)"
    )
    public ResponseEntity<User> addUserToCourse(
        @PathVariable Long courseId,
        @PathVariable Long userId
    ) {
        LOG.debug("Adding user to course with id {}", courseId);
        User user = courseService.addUserToCourse(courseId, userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Updates the course with the given id with the new course details.
     *
     * @param id               the id of the existing course.
     * @param courseDetailsDto the new details of the course.
     * @return 200 and the changed course if the action was successful,
     * 404 if the course was not found.
     */
    @PutMapping("/{id}")
    @PreAuthorize(
        "hasRole('ADMIN') " +
        "or (hasRole('TEACHER') and @authenticationCheckerService.isUserEnrolledInCourse(#id))"
    )
    public ResponseEntity<Course> updateCourse(
        @PathVariable Long id,
        @Valid @RequestBody CourseDto courseDetailsDto
    ) {
        LOG.debug("Updating course with id {}", id);
        Course course = courseService.updateCourse(id,
            courseMapper.convertDtoToEntity(courseDetailsDto));
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    /**
     * Deletes all existing courses.
     *
     * @return 200 and a message if the deletion was successful.
     */
    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAllCourses() {
        LOG.debug("Deleting all courses");
        courseService.deleteAllCourses();
        return new ResponseEntity<>("All courses have been deleted successfully", HttpStatus.OK);
    }

    /**
     * Deletes a course by its id.
     *
     * @param id the id of the course to be deleted.
     * @return 200 and a message if the deletion was successful,
     * 404 if the course was not found.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        LOG.debug("Deleting course with id {}", id);
        courseService.deleteCourse(id);
        return new ResponseEntity<>("Course deleted successfully", HttpStatus.OK);
    }

    /**
     * Removes a user from a course.
     *
     * @param courseId the id of the course.
     * @param userId   the id of the {@code User} to be removed from the course.
     * @return 200 with the user if the action was successful,
     * 404 if the user or the course weren't found.
     */
    @DeleteMapping("/{courseId}/users/{userId}")
    @PreAuthorize(
        "hasRole('ADMIN') " +
        "or (hasRole('TEACHER') and @authenticationCheckerService.isUserEnrolledInCourse(#courseId)) " +
        "or @authenticationCheckerService.doUserIdsMatch(#userId)"
    )
    public ResponseEntity<User> removeUserFromCourse(
        @PathVariable Long courseId,
        @PathVariable Long userId
    ) {
        LOG.debug("Removing user from course with id {}", courseId);
        User user = courseService.removeUserFromCourse(courseId, userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
