package com.yoanpetrov.studentmanagementsystem.model;

import com.yoanpetrov.studentmanagementsystem.security.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    @OneToMany(mappedBy = "user") // it is mapped by the user field in StudentCourseEnrollment
    Set<StudentCourseEnrollment> enrollments;

    private String firstName;
    private String lastName;
    private String email;
    private String pass;
    @Enumerated(EnumType.STRING)
    private Role role;


}
