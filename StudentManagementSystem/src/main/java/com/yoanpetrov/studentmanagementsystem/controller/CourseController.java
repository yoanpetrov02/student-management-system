package com.yoanpetrov.studentmanagementsystem.controller;

import com.yoanpetrov.studentmanagementsystem.model.Course;
import com.yoanpetrov.studentmanagementsystem.model.User;
import com.yoanpetrov.studentmanagementsystem.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public Optional<Course> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @GetMapping("/{id}/users")
    public List<User> getAllCourseUsers(@PathVariable Long id) {
        return courseService.getAllCourseUsers(id);
    }

    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    /**
     * Adds a user to a course
     *
     * @param id the course id.
     * @param requestUser the user to be added to the course.
     * @return the added user todo everything has to change to use response codes and stuff.
     */
    @PostMapping("/{id}/users")
    public User addUserToCourse(@PathVariable Long id, @RequestBody User requestUser) {
        User user = courseService.addUserToCourse(id, requestUser);
        return user;
    }

    @PutMapping("/{id}")
    public Course updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        return courseService.updateCourse(id, courseDetails);
    }

    @DeleteMapping
    public String deleteAllCourses() {
        courseService.deleteAllCourses();
        return "All users have been deleted successfully.";
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

    @DeleteMapping("/{id}/users")
    public User removeUserFromCourse(@PathVariable Long id, @RequestBody User requestUser) {
        User user = courseService.removeUserFromCourse(id, requestUser);
        return user;
    }
}
