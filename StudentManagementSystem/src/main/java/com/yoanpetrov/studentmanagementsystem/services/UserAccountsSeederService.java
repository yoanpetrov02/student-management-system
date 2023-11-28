package com.yoanpetrov.studentmanagementsystem.services;

import com.yoanpetrov.studentmanagementsystem.entities.User;
import com.yoanpetrov.studentmanagementsystem.entities.UserAccount;
import com.yoanpetrov.studentmanagementsystem.repositories.UserAccountRepository;
import com.yoanpetrov.studentmanagementsystem.security.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Profile("!test")
@Service
@RequiredArgsConstructor
@Slf4j
public class UserAccountsSeederService implements CommandLineRunner {

    private final UserAccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (accountsAlreadyExisting()) {
            return;
        }
        log.debug("Seeding accounts in the database.");
        UserAccount adminAccount = UserAccount.builder()
            .username("admin")
            .password(passwordEncoder.encode("admin"))
            .role(Role.ADMIN)
            .user(new User())
            .build();
        UserAccount teacherAccount = UserAccount.builder()
            .username("teacher")
            .password(passwordEncoder.encode("teacher"))
            .role(Role.TEACHER)
            .user(new User())
            .build();
        UserAccount studentAccount = UserAccount.builder()
            .username("student")
            .password(passwordEncoder.encode("student"))
            .role(Role.STUDENT)
            .user(new User())
            .build();
        repository.saveAll(List.of(adminAccount, teacherAccount, studentAccount));
    }

    private boolean accountsAlreadyExisting() {
        return repository.existsByUsername("admin")
            || repository.existsByUsername("teacher")
            || repository.existsByUsername("student");
    }
}
