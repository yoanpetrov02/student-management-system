package com.yoanpetrov.studentmanagementsystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "studentsCoursesEnrollments")
public class StudentCourseEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String enrollmentId;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "userId")
    User user;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "courseId")
    Course course;

    public String getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(String enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
