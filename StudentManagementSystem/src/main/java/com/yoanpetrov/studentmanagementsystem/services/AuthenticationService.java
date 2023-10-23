package com.yoanpetrov.studentmanagementsystem.services;

import com.yoanpetrov.studentmanagementsystem.entities.User;
import com.yoanpetrov.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.yoanpetrov.studentmanagementsystem.entities.UserAccount;
import com.yoanpetrov.studentmanagementsystem.repositories.UserAccountRepository;
import com.yoanpetrov.studentmanagementsystem.repositories.UserRepository;
import com.yoanpetrov.studentmanagementsystem.rest.dto.UserAccountDto;
import com.yoanpetrov.studentmanagementsystem.security.AuthenticationResponse;
import com.yoanpetrov.studentmanagementsystem.security.Role;
import com.yoanpetrov.studentmanagementsystem.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final UserAccountRepository accountRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public boolean checkUserExistence(String username) {
        return accountRepository.existsByUsername(username);
    }

    public AuthenticationResponse registerAccount(UserAccountDto userAccountDto) {
        String username = userAccountDto.getUsername();
        String password = userAccountDto.getPassword();

        UserAccount userAccount = UserAccount.builder()
            .username(username)
            .password(encoder.encode(password))
            .role(Role.USER)
            .build();
        userAccount.setUser(new User()); // create a new User for each UserAccount
        accountRepository.save(userAccount);

        var jwtToken = jwtService.generateToken(userAccount);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticateUser(UserAccountDto userAccountDto) {
        var userAccount = accountRepository.findByUsername(userAccountDto.getUsername())
            .orElseThrow(() -> new ResourceNotFoundException("The user account does not exist"));
        if (!encoder.matches(userAccountDto.getPassword(), userAccount.getPassword())) {
            throw new BadCredentialsException("Wrong username or password");
        }
        return new AuthenticationResponse(jwtService.generateToken(userAccount));
    }
}
