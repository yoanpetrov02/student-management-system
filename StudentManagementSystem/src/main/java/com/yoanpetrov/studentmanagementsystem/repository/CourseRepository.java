package com.yoanpetrov.studentmanagementsystem.repository;

import com.yoanpetrov.studentmanagementsystem.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findCoursesByUsersUserId(Long id);
}
