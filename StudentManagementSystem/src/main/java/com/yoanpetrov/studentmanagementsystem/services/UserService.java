package com.yoanpetrov.studentmanagementsystem.services;

import com.yoanpetrov.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.yoanpetrov.studentmanagementsystem.entities.Course;
import com.yoanpetrov.studentmanagementsystem.entities.User;
import com.yoanpetrov.studentmanagementsystem.repositories.CourseRepository;
import com.yoanpetrov.studentmanagementsystem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User service. Used to perform business logic on users.
 */
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    /**
     * Checks whether a user exists in the database.
     *
     * @param userId the id of the user.
     * @return true if the user exists, false otherwise.
     */
    public boolean existsUser(Long userId) {
        return userRepository.existsById(userId);
    }

    /**
     * Gets all existing users in the database.
     *
     * @return a list of the users, empty if no users exist.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Gets a single user from the database by its id.
     *
     * @param id the id of the user.
     * @return the user, if it exists.
     * @throws ResourceNotFoundException if the user was not found.
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    /**
     * Gets all courses the given user is enrolled in.
     *
     * @param id the id of the user.
     * @return a list of the courses, empty if the user is not enrolled in any courses.
     * @throws ResourceNotFoundException if the user was not found.
     */
    public List<Course> getAllUserCourses(Long id) {
        if (!existsUser(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        return courseRepository.findCoursesByUsersUserId(id);
    }

    /**
     * Inserts the given user into the database.
     *
     * @param user the user to be created.
     * @return the saved user in the database.
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Adds the given course to the user with the given id.
     *
     * @param userId      the id of the user.
     * @param courseToAdd the course to be added to the user.
     * @return the added {@code Course} if the action was successful.
     * @throws ResourceNotFoundException if the user or course were not found.
     */
    public Course addCourseToUser(Long userId, Course courseToAdd) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Course course = courseRepository.findById(courseToAdd.getCourseId())
            .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        course.addUser(user);
        userRepository.save(user);
        return course;
    }

    /**
     * Updates the user with the given id with the new user details.
     *
     * @param id          the id of the user.
     * @param userDetails the new user details.
     * @return the updated user.
     * @throws ResourceNotFoundException if the user was not found.
     */
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());

        return userRepository.save(user);
    }

    /**
     * Deletes all users.
     */
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    /**
     * Deletes a user by its id.
     *
     * @param id the id of the user.
     * @throws ResourceNotFoundException if the user was not found.
     */
    public void deleteUser(Long id) {
        if (!existsUser(id)) {
            throw new ResourceNotFoundException("Course not found");
        }
        userRepository.deleteById(id);
    }

    /**
     * Removes the given course from the user with the given id.
     *
     * @param userId         the id of the user.
     * @param courseToRemove the course to be removed from the user.
     * @return the removed {@code Course} if the action was successful.
     * @throws ResourceNotFoundException if the user or course were not found.
     */
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
