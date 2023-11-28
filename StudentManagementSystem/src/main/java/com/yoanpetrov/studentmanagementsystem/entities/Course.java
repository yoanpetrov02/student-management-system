package com.yoanpetrov.studentmanagementsystem.entities;

import com.yoanpetrov.studentmanagementsystem.exceptions.ResourceConflictException;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a course.
 * Each course can have multiple users.
 * A course can have students added to/removed from it.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue
    private Long courseId;
    private String name;
    private String description;
    private int maxCapacity;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "students_courses_enrollments",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<>();

    /**
     * Returns the number of students in the users list of the course.
     */
    public int getNumberOfStudents() {
        return users.size();
    }

    /**
     * Adds a {@code User} to the course.
     *
     * @param user the user to be added.
     * @throws ResourceConflictException if the course is full or if the user already exists in the course.
     */
    public void addUser(User user) {
        if (users.size() == maxCapacity) {
            throw new ResourceConflictException("The course is full");
        }
        if (users.contains(user)) {
            throw new ResourceConflictException("The user already exists in the course");
        }
        users.add(user);
        user.getCourses().add(this);
    }

    /**
     * Removes a {@code User} from the course.
     *
     * @param user the user to be removed.
     * @throws ResourceConflictException if the user is not present in the course.
     */
    public void removeUser(User user) {
        if (!users.remove(user)) {
            throw new ResourceConflictException("The user is not present in the course");
        }
        user.getCourses().remove(this);
    }
}
