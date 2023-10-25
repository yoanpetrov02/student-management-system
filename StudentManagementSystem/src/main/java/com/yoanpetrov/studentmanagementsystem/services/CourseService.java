package com.yoanpetrov.studentmanagementsystem.services;

import com.yoanpetrov.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.yoanpetrov.studentmanagementsystem.entities.Course;
import com.yoanpetrov.studentmanagementsystem.entities.User;
import com.yoanpetrov.studentmanagementsystem.repositories.CourseRepository;
import com.yoanpetrov.studentmanagementsystem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Course service. Used to perform business logic on courses.
 */
@RequiredArgsConstructor
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    /**
     * Checks whether a course exists in the database.
     *
     * @param courseId the id of the course.
     * @return true if the course exists, false otherwise.
     */
    public boolean existsCourse(Long courseId) {
        return courseRepository.existsById(courseId);
    }

    /**
     * Gets all existing courses in the database.
     *
     * @return a list of the courses, empty if no courses exist.
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Gets a single course from the database by its id.
     *
     * @param id the id of the course.
     * @return the course, if it exists.
     * @throws ResourceNotFoundException if the course was not found.
     */
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    /**
     * Gets all users enrolled in the given course.
     *
     * @param id the id of the course.
     * @return a list of the users, empty if no users exist in the course.
     * @throws ResourceNotFoundException if the course was not found.
     */
    public List<User> getAllCourseUsers(Long id) {
        if (!existsCourse(id)) {
            throw new ResourceNotFoundException("Course not found");
        }
        return userRepository.findUsersByCoursesCourseId(id);
    }

    /**
     * Inserts the given course into the database.
     *
     * @param course the course to be created.
     * @return the saved course in the database.
     */
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    /**
     * Adds the given user to the course with the given id.
     *
     * @param courseId the id of the course.
     * @param userToAdd the user to be added to the course.
     * @return the added {@code User} if the action was successful.
     * @throws ResourceNotFoundException if the user or course were not found.
     */
    public User addUserToCourse(Long courseId, User userToAdd) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        User user = userRepository.findById(userToAdd.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        course.addUser(user);
        courseRepository.save(course);
        return user;
    }

    /**
     * Updates the course with the given id with the new course details.
     *
     * @param id the id of the course.
     * @param courseDetails the new course details.
     * @return the updated course.
     * @throws ResourceNotFoundException if the course was not found.
     */
    public Course updateCourse(Long id, Course courseDetails) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        course.setName(courseDetails.getName());
        course.setDescription(courseDetails.getDescription());
        course.setMaxCapacity(courseDetails.getMaxCapacity());
        course.setNumberOfStudents(courseDetails.getNumberOfStudents());

        return courseRepository.save(course);
    }

    /**
     * Deletes all courses.
     */
    public void deleteAllCourses() {
        courseRepository.deleteAll();
    }

    /**
     * Deletes a course by its id.
     *
     * @param id the id of the course.
     * @throws ResourceNotFoundException if the course was not found.
     */
    public void deleteCourse(Long id) {
        if (!existsCourse(id)) {
            throw new ResourceNotFoundException("Course not found");
        }
        courseRepository.deleteById(id);
    }

    /**
     * Removes the given user from the course with the given id.
     *
     * @param courseId the id of the course.
     * @param userToRemove the user to be removed from the course.
     * @return the removed {@code User} if the action was successful.
     * @throws ResourceNotFoundException if the user or course were not found.
     */
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
