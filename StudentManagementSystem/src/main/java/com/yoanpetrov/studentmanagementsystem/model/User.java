package com.yoanpetrov.studentmanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yoanpetrov.studentmanagementsystem.security.Role;
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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long userId;

    @Column(nullable = false, length = 100)
    private String firstName;
    @Column(nullable = false, length = 100)
    private String lastName;
    private String email;
    @Column(nullable = false, length = 100)
    private String password;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(mappedBy = "users")
    @JsonIgnore // do not remove unless you want to create a StackOverflow
    private List<Course> courses = new ArrayList<>();
}
