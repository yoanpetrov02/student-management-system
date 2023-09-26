package com.yoanpetrov.studentmanagementsystem.service;

import com.yoanpetrov.studentmanagementsystem.model.StudentCourseEnrollment;
import com.yoanpetrov.studentmanagementsystem.repository.StudentCourseEnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StudentCourseEnrollmentService {

    private final StudentCourseEnrollmentRepository repository;

    public StudentCourseEnrollment createEnrollment(StudentCourseEnrollment enrollment) {
        return repository.save(enrollment);
    }

    public List<StudentCourseEnrollment> getAllEnrollments() {
        return repository.findAll();
    }

    public Optional<StudentCourseEnrollment> getEnrollmentById(String id) {
        return repository.findById(id);
    }

    public StudentCourseEnrollment updateEnrollment(String id, StudentCourseEnrollment enrollmentDetails) {
        Optional<StudentCourseEnrollment> enrollment = repository.findById(id);
        if (enrollment.isEmpty()) {
            return null;
        }
        StudentCourseEnrollment existingEnrollment = enrollment.get();
        existingEnrollment.setUser(enrollmentDetails.getUser());
        existingEnrollment.setCourse(enrollmentDetails.getCourse());

        return repository.save(existingEnrollment);
    }

    public void deleteAllEnrollments() {
        repository.deleteAll();
    }

    public void deleteEnrollment(String id) {
        repository.deleteById(id);
    }
}
