package com.yoanpetrov.studentmanagementsystem.service;

import com.yoanpetrov.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.yoanpetrov.studentmanagementsystem.model.UserAccount;
import com.yoanpetrov.studentmanagementsystem.repository.UserAccountRepository;
import com.yoanpetrov.studentmanagementsystem.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final UserAccountRepository repository;
    private final PasswordEncoder encoder;

    public boolean checkUserExistence(String username) {
        return repository.existsByUsername(username);
    }

    public UserAccount createUserAccount(String username, String password) {
        String hashed = encoder.encode(password);
        UserAccount userAccount = UserAccount.builder()
            .username(username)
            .password(hashed)
            .role(Role.USER)
            .build();

        return repository.save(userAccount);
    }

    public boolean validatePassword(String username, String password) {
        UserAccount accountFromDb = repository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("The user account does not exist"));

        return encoder.matches(password, accountFromDb.getPassword());
    }
}
