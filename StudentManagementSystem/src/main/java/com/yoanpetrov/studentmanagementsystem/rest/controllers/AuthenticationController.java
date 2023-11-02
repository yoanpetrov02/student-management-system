package com.yoanpetrov.studentmanagementsystem.rest.controllers;

import com.yoanpetrov.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.yoanpetrov.studentmanagementsystem.rest.dto.UserAccountDto;
import com.yoanpetrov.studentmanagementsystem.security.AuthenticationResponse;
import com.yoanpetrov.studentmanagementsystem.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            return new ResponseEntity<>(HttpStatus.CONFLICT);
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
        try {
            AuthenticationResponse response = authenticationService.authenticateUser(userDto);
            LOG.debug("Successful authentication, generated token is {}", response.getAccessToken());
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            LOG.debug("Wrong password, returning 401");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (ResourceNotFoundException e) {
            LOG.debug("User not found, returning 404");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(
        HttpServletRequest request
    ) {
        try {
            return ResponseEntity.ok(authenticationService.refreshToken(request));
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
