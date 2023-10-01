package com.yoanpetrov.studentmanagementsystem.model;

import com.yoanpetrov.studentmanagementsystem.exceptions.ResourceConflictException;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue
    private Long courseId;

    @Column(nullable = false, length = 100)
    private String name;
    @Column
    private String description;
    @Column(nullable = false)
    private int maxCapacity;
    @Column(nullable = false)
    private int numberOfStudents;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "students_courses_enrollments",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<>();

    public void addUser(User user) {
        if (numberOfStudents == maxCapacity) {
            throw new ResourceConflictException("The course is full");
        }
        if (users.contains(user)) {
            throw new ResourceConflictException("The user already exists in the course");
        }
        users.add(user);
        user.getCourses().add(this);
        numberOfStudents++;
    }

    public void removeUser(User user) {
        if (!users.remove(user)) { // user wasn't present in the list
            throw new ResourceConflictException("The user is not present in the course");
        }
        user.getCourses().remove(this);
        numberOfStudents--;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return maxCapacity == course.maxCapacity
                && numberOfStudents == course.numberOfStudents
                && Objects.equals(courseId, course.courseId)
                && Objects.equals(name, course.name)
                && Objects.equals(description, course.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, name, description, maxCapacity, numberOfStudents);
    }
}
