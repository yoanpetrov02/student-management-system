package com.yoanpetrov.studentmanagementsystem.service;

import com.yoanpetrov.studentmanagementsystem.model.UserAccount;
import com.yoanpetrov.studentmanagementsystem.repository.UserAccountRepository;
import com.yoanpetrov.studentmanagementsystem.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RegisterService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder encoder;

    public boolean checkUserExistence(String username) {
        return userAccountRepository.existsByUsername(username);
    }

    public UserAccount createUserAccount(String username, String password) {
        String hashed = encoder.encode(password);
        UserAccount userAccount = UserAccount.builder()
            .username(username)
            .password(hashed)
            .role(Role.USER)
            .build();

        return userAccountRepository.save(userAccount);
    }
}
