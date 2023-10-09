package com.yoanpetrov.studentmanagementsystem.service;

import com.yoanpetrov.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.yoanpetrov.studentmanagementsystem.model.UserAccount;
import com.yoanpetrov.studentmanagementsystem.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class LoginService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder encoder;

    public boolean checkUserExistence(UserAccount userAccount) {
        return userAccountRepository.existsById(userAccount.getAccountId());
    }

    public boolean validatePassword(UserAccount userAccount) {
        String hashed = encoder.encode(userAccount.getPassword());
        UserAccount accountFromDb = userAccountRepository.findById(userAccount.getAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("The user account does not exist"));

        return Objects.equals(hashed, accountFromDb.getPassword());
    }
}
