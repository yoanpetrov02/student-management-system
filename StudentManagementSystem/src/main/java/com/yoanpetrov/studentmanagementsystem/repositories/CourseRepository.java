package com.yoanpetrov.studentmanagementsystem.repositories;

import com.yoanpetrov.studentmanagementsystem.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findCoursesByUsersUserId(Long id);
}
