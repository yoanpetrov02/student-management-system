package com.yoanpetrov.studentmanagementsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "students_courses_enrollments")
public class StudentCourseEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int enrollmentId;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    Course course;
}
