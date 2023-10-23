package com.yoanpetrov.studentmanagementsystem.repositories;

import com.yoanpetrov.studentmanagementsystem.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    boolean existsByUsername(String username);

    Optional<UserAccount> findByUsername(String username);
}
