package com.yoanpetrov.studentmanagementsystem.services;

import com.yoanpetrov.studentmanagementsystem.entities.User;
import com.yoanpetrov.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.yoanpetrov.studentmanagementsystem.entities.UserAccount;
import com.yoanpetrov.studentmanagementsystem.repositories.UserAccountRepository;
import com.yoanpetrov.studentmanagementsystem.dto.UserAccountDto;
import com.yoanpetrov.studentmanagementsystem.dto.AuthenticationResponse;
import com.yoanpetrov.studentmanagementsystem.security.Role;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Authentication service. User to register and authenticate users.
 */
@RequiredArgsConstructor
@Service
public class AuthenticationService implements CommandLineRunner {

    private final UserAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
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
            .password(passwordEncoder.encode(password))
            .role(Role.STUDENT)
            .build();
        userAccount.setUser(new User()); // create a new User for each UserAccount
        accountRepository.save(userAccount);

        var jwtToken = jwtService.generateToken(convertToUserDetails(userAccount));

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
        var user = accountRepository.findByUsername(userAccountDto.getUsername())
            .orElseThrow(() -> new ResourceNotFoundException("The user account does not exist"));

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                userAccountDto.getUsername(),
                userAccountDto.getPassword()));

        var jwtToken = jwtService.generateToken(convertToUserDetails(user));

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
           UserDetails userDetails = convertToUserDetails(user);
           if (jwtService.validateToken(oldJwtToken, userDetails)) {
               var refreshToken = jwtService.generateRefreshToken(userDetails);
               return AuthenticationResponse.builder()
                   .accessToken("")
                   .refreshToken(refreshToken).build();
           }
       }
       return new AuthenticationResponse("", "");
    }

    @Override
    public void run(String... args) {
        UserAccount adminAccount = UserAccount.builder()
            .username("admin")
            .password(passwordEncoder.encode("admin"))
            .role(Role.ADMIN).build();
        accountRepository.save(adminAccount);
    }

    private UserDetails convertToUserDetails(UserAccount account) {
        return new org.springframework.security.core.userdetails.User(
            account.getUsername(),
            account.getPassword(),
            true, true, true, true,
            account.getRole().getAuthorities());
    }
}
