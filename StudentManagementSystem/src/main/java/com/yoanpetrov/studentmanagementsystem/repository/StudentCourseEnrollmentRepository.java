package com.yoanpetrov.studentmanagementsystem.repository;

import com.yoanpetrov.studentmanagementsystem.model.StudentCourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentCourseEnrollmentRepository
        extends JpaRepository<StudentCourseEnrollment, Long> {
}
