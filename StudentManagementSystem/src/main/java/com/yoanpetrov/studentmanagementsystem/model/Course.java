package com.yoanpetrov.studentmanagementsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany
    @JoinTable(name = "students_courses_enrollments",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<>();
}
