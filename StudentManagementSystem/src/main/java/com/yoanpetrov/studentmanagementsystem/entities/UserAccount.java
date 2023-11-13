package com.yoanpetrov.studentmanagementsystem.entities;

import com.yoanpetrov.studentmanagementsystem.security.Role;
import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a user account. Each user account is connected to a single user.
 * This class implements {@code UserDetails}, an interface used by Spring Security
 * to manage authentication and authorization.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_accounts")
public class UserAccount {

    @Id
    @GeneratedValue
    private long accountId;
    @Column(unique = true)
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
}
