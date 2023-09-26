package com.yoanpetrov.studentmanagementsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String courseId;

    @OneToMany(mappedBy = "course") // it is mapped by the course field in StudentCourseEnrollment
    Set<StudentCourseEnrollment> studentEnrollments;

    private String name;
    private String description;
    private int maxCapacity;
    private int numberOfStudents;
}
