package com.yoanpetrov.studentmanagementsystem.repositories;

import com.yoanpetrov.studentmanagementsystem.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findCoursesByUsersUserId(Long id);
}
