package com.yoanpetrov.studentmanagementsystem.service;

import com.yoanpetrov.studentmanagementsystem.model.Course;
import com.yoanpetrov.studentmanagementsystem.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository repository;

    public Course createCourse(Course course) {
        return repository.save(course);
    }

    public List<Course> getAllCourses() {
        return repository.findAll();
    }

    public Optional<Course> getCourseById(String id) {
        return repository.findById(id);
    }

    public Course updateCourse(String id, Course courseDetails) {
        Optional<Course> course = repository.findById(id);
        if (course.isEmpty()) {
            return null;
        }
        Course existingCourse = course.get();
        existingCourse.setName(courseDetails.getName());
        existingCourse.setDescription(courseDetails.getDescription());
        existingCourse.setMaxCapacity(courseDetails.getMaxCapacity());
        existingCourse.setNumberOfStudents(courseDetails.getNumberOfStudents());

        return repository.save(existingCourse);
    }

    public void deleteAllCourses() {
        repository.deleteAll();
    }

    public void deleteCourse(String id) {
        repository.deleteById(id);
    }
}
