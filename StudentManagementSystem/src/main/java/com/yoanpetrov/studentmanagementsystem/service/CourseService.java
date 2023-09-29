package com.yoanpetrov.studentmanagementsystem.service;

import com.yoanpetrov.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.yoanpetrov.studentmanagementsystem.model.Course;
import com.yoanpetrov.studentmanagementsystem.model.User;
import com.yoanpetrov.studentmanagementsystem.repository.CourseRepository;
import com.yoanpetrov.studentmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public boolean existsCourse(Long courseId) {
        return courseRepository.existsById(courseId);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public List<User> getAllCourseUsers(Long id) {
        return userRepository.findUsersByCoursesCourseId(id);
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public User addUserToCourse(Long courseId, User userToAdd) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        User user = userRepository.findById(userToAdd.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        course.addUser(user);
        courseRepository.save(course);
        return user;
    }

    public Course updateCourse(Long id, Course courseDetails) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isEmpty()) {
            return null;
        }
        Course existingCourse = course.get();
        existingCourse.setName(courseDetails.getName());
        existingCourse.setDescription(courseDetails.getDescription());
        existingCourse.setMaxCapacity(courseDetails.getMaxCapacity());
        existingCourse.setNumberOfStudents(courseDetails.getNumberOfStudents());

        return courseRepository.save(existingCourse);
    }

    public void deleteAllCourses() {
        courseRepository.deleteAll();
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public User removeUserFromCourse(Long courseId, User userToRemove) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        User user = userRepository.findById(userToRemove.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        course.removeUser(user);
        courseRepository.save(course);
        return user;
    }
}
