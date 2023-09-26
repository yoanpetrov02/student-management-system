package com.yoanpetrov.studentmanagementsystem.repository;

import com.yoanpetrov.studentmanagementsystem.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
