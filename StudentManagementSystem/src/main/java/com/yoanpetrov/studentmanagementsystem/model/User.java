package com.yoanpetrov.studentmanagementsystem.model;

import com.yoanpetrov.studentmanagementsystem.security.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;

    @OneToMany(mappedBy = "user") // it is mapped by the user field in StudentCourseEnrollment
    private Set<StudentCourseEnrollment> enrollments;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
}
