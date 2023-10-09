package com.yoanpetrov.studentmanagementsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yoanpetrov.studentmanagementsystem.security.Role;
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

    @ManyToMany(mappedBy = "users", cascade = CascadeType.REMOVE)
    @JsonIgnore // do not remove unless you want to create a StackOverflow
    private List<Course> courses = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private UserAccount userAccount = new UserAccount();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId)
                && Objects.equals(firstName, user.firstName)
                && Objects.equals(lastName, user.lastName)
                && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, firstName, lastName, email);
    }
}
