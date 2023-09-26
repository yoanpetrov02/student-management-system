package com.yoanpetrov.studentmanagementsystem.controller;

import com.yoanpetrov.studentmanagementsystem.model.StudentCourseEnrollment;
import com.yoanpetrov.studentmanagementsystem.service.StudentCourseEnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/enrollments")
public class StudentCourseEnrollmentController {

    private final StudentCourseEnrollmentService enrollmentService;

    @PostMapping
    public StudentCourseEnrollment createEnrollment(@RequestBody StudentCourseEnrollment enrollment) {
        return enrollmentService.createEnrollment(enrollment);
    }

    @GetMapping
    public List<StudentCourseEnrollment> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    @GetMapping("/{id}")
    public Optional<StudentCourseEnrollment> getEnrollmentById(@PathVariable int id) {
        return enrollmentService.getEnrollmentById(id);
    }

    @PutMapping("/{id}")
    public StudentCourseEnrollment updateEnrollment(@PathVariable int id, @RequestBody StudentCourseEnrollment enrollmentDetails) {
        return enrollmentService.updateEnrollment(id, enrollmentDetails);
    }

    @DeleteMapping
    public String deleteAllEnrollments() {
        enrollmentService.deleteAllEnrollments();
        return "All users have been deleted successfully.";
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable int id) {
        enrollmentService.deleteEnrollment(id);
    }
}
