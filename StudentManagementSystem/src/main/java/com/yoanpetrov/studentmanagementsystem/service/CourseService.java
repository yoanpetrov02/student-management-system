package com.yoanpetrov.studentmanagementsystem.service;

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

    /**
     * Gets the course and the user from the DB, adds the User to the course's
     * user list and adds the course to the user's course list.
     *
     * @param courseId the id of the course.
     * @param userToAdd the user to be added.
     * @return the User that was added.
     */
    public User addUserToCourse(Long courseId, User userToAdd) {
        User user = courseRepository.findById(courseId).map(course -> {
            Long userId = userToAdd.getUserId();
            User userFromDb = userRepository.findById(userId).get();
            course.getUsers().add(userFromDb);
            userFromDb.getCourses().add(course);
            courseRepository.save(course);
            return userFromDb;
        }).get();
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

    public User removeUserFromCourse(Long id, User userToRemove) {
        User user = courseRepository.findById(id).map(course -> {
            Long userId = userToRemove.getUserId();
            User userFromDb = userRepository.findById(userId).get();
            course.getUsers().remove(userFromDb);
            userFromDb.getCourses().remove(course);
            courseRepository.save(course);
            return userFromDb;
        }).get();
        return user;
    }
}
