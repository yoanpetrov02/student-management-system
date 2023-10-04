package com.yoanpetrov.studentmanagementsystem.service;

import com.yoanpetrov.studentmanagementsystem.exceptions.ResourceConflictException;
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
public class UserService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public boolean existsUser(Long userId) {
        return userRepository.existsById(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<Course> getAllUserCourses(Long id) {
        return courseRepository.findCoursesByUsersUserId(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Course addCourseToUser(Long userId, Course courseToAdd) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Course course = courseRepository.findById(courseToAdd.getCourseId())
            .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        course.addUser(user);
        userRepository.save(user);
        return course;
    }

    public User updateUser(Long id, User userDetails) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return null;
        }
        User existingUser = user.get();
        existingUser.setFirstName(userDetails.getFirstName());
        existingUser.setLastName(userDetails.getLastName());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setPassword(userDetails.getPassword());
        existingUser.setRole(userDetails.getRole());

        return userRepository.save(existingUser);
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Course removeCourseFromUser(Long userId, Course courseToRemove) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Course course = courseRepository.findById(courseToRemove.getCourseId())
            .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        course.removeUser(user);
        userRepository.save(user);
        return course;
    }
}
