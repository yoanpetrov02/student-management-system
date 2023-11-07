package com.yoanpetrov.studentmanagementsystem.rest.controllers;

import com.yoanpetrov.studentmanagementsystem.exceptions.ResourceConflictException;
import com.yoanpetrov.studentmanagementsystem.rest.dto.UserAccountDto;
import com.yoanpetrov.studentmanagementsystem.security.AuthenticationResponse;
import com.yoanpetrov.studentmanagementsystem.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1")
public class AuthenticationController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;

    /**
     * Registers the given user.
     *
     * @param userDto the credentials of the user's account.
     * @return 200 and a JWT token if the user was successfully registered,
     * 409 if a user account with this username already exists.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody UserAccountDto userDto) {
        LOG.debug("Attempting to register user account");
        if (authenticationService.checkUserExistence(userDto.getUsername())) {
            LOG.debug("Account with the same username already exists, returning 409");
            throw new ResourceConflictException("An account with the same username already exists");
        }
        AuthenticationResponse response = authenticationService.registerAccount(userDto);
        LOG.debug("Successful registration, generated token is {}", response.getAccessToken());
        return ResponseEntity.ok(response);
    }

    /**
     * Authenticates the given user.
     *
     * @param userDto the credentials of the user's account.
     * @return 200 and a JWT token if the authentication was successful,
     * 404 if the user account doesn't exist,
     * 401 if the credentials weren't correct.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody UserAccountDto userDto) {
        LOG.debug("Attempting to authenticate user account: {}", userDto.getUsername());
        AuthenticationResponse response = authenticationService.authenticateUser(userDto);
        LOG.debug("Successful authentication, generated token is {}", response.getAccessToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }
}
