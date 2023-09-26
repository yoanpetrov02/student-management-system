package com.yoanpetrov.studentmanagementsystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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

    @OneToMany(mappedBy = "course") // it is mapped by the course field in StudentCourseEnrollment
    private Set<StudentCourseEnrollment> studentEnrollments;
}
