package com.yoanpetrov.studentmanagementsystem.services;

import com.yoanpetrov.studentmanagementsystem.entities.User;
import com.yoanpetrov.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.yoanpetrov.studentmanagementsystem.entities.UserAccount;
import com.yoanpetrov.studentmanagementsystem.repositories.UserAccountRepository;
import com.yoanpetrov.studentmanagementsystem.rest.dto.UserAccountDto;
import com.yoanpetrov.studentmanagementsystem.security.AuthenticationResponse;
import com.yoanpetrov.studentmanagementsystem.security.Role;
import com.yoanpetrov.studentmanagementsystem.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Authentication service. User to register and authenticate users.
 */
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final UserAccountRepository accountRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Checks whether a user account exists in the database.
     *
     * @param username the username of the account.
     * @return true if the account exists, false otherwise.
     */
    public boolean checkUserExistence(String username) {
        return accountRepository.existsByUsername(username);
    }

    /**
     * Registers the given user account, saving it to the database.
     *
     * @param userAccountDto the account to register.
     * @return an {@code AuthenticationResponse} with a JWT token when the account gets created.
     */
    public AuthenticationResponse registerAccount(UserAccountDto userAccountDto) {
        String username = userAccountDto.getUsername();
        String password = userAccountDto.getPassword();

        UserAccount userAccount = UserAccount.builder()
            .username(username)
            .password(encoder.encode(password))
            .role(Role.STUDENT)
            .build();
        userAccount.setUser(new User()); // create a new User for each UserAccount
        accountRepository.save(userAccount);

        var jwtToken = jwtService.generateToken(userAccount);

        return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken("")
            .build();
    }

    /**
     * Authenticates the given user account.
     *
     * @param userAccountDto the user's credentials.
     * @return an {@code AuthenticationResponse} with a JWT token if the account is successfully authenticated.
     * @throws BadCredentialsException   if the password is wrong.
     * @throws ResourceNotFoundException if a user account with that username does not exist.
     */
    public AuthenticationResponse authenticateUser(UserAccountDto userAccountDto) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                userAccountDto.getUsername(),
                userAccountDto.getPassword()));
        var user = accountRepository.findByUsername(userAccountDto.getUsername())
            .orElseThrow(() -> new ResourceNotFoundException("The user account does not exist"));

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken("")
            .build();
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request) {
       final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
       final String oldJwtToken;
       final String username;

       if (authHeader == null || !authHeader.startsWith("Bearer ")) {
           return new AuthenticationResponse("", "");
       }
       oldJwtToken = authHeader.substring(7);
       username = jwtService.extractUsername(oldJwtToken);
       if (username != null) {
           var user = accountRepository.findByUsername(username)
               .orElseThrow(() -> new ResourceNotFoundException("User account not found"));
           if (jwtService.isTokenValid(oldJwtToken, user)) {
               var refreshToken = jwtService.generateRefreshToken(user);
               return AuthenticationResponse.builder()
                   .accessToken("")
                   .refreshToken(refreshToken).build();
           }
       }
       return new AuthenticationResponse("", "");
    }
}
