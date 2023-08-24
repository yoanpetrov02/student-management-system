package com.yoanpetrov.studentmanagementsystem.controller;

import com.yoanpetrov.studentmanagementsystem.model.StudentCourseEnrollment;
import com.yoanpetrov.studentmanagementsystem.service.StudentCourseEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/enrollments")
public class StudentCourseEnrollmentController {

    @Autowired
    private StudentCourseEnrollmentService enrollmentService;

    @PostMapping
    public StudentCourseEnrollment createEnrollment(@RequestBody StudentCourseEnrollment enrollment) {
        return enrollmentService.createEnrollment(enrollment);
    }

    @GetMapping
    public List<StudentCourseEnrollment> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    @GetMapping("/{id}")
    public Optional<StudentCourseEnrollment> getEnrollmentById(@PathVariable String id) {
        return enrollmentService.getEnrollmentById(id);
    }

    @PutMapping("/{id}")
    public StudentCourseEnrollment updateEnrollment(@PathVariable String id, @RequestBody StudentCourseEnrollment enrollmentDetails) {
        return enrollmentService.updateEnrollment(id, enrollmentDetails);
    }

    @DeleteMapping
    public String deleteAllEnrollments() {
        enrollmentService.deleteAllEnrollments();
        return "All users have been deleted successfully.";
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable String id) {
        enrollmentService.deleteEnrollment(id);
    }
}
