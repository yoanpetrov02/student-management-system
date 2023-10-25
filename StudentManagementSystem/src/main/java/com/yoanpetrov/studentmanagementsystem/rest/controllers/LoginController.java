package com.yoanpetrov.studentmanagementsystem.rest.controllers;

import com.yoanpetrov.studentmanagementsystem.rest.dto.UserAccountDto;
import com.yoanpetrov.studentmanagementsystem.security.AuthenticationResponse;
import com.yoanpetrov.studentmanagementsystem.services.AuthenticationService;
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

/**
 * Rest controller for the login endpoints.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/login")
public class LoginController {

    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    private final AuthenticationService authenticationService;

    /**
     * Authenticates the given user.
     *
     * @param userDto the credentials of the user's account.
     * @return 200 and a JWT token if the authentication was successful,
     * 404 if the user account doesn't exist,
     * 401 if the credentials weren't correct.
     */
    @PostMapping
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody UserAccountDto userDto) {
        LOG.debug("Attempting to authenticate user account: {}", userDto.getUsername());
        if (!authenticationService.checkUserExistence(userDto.getUsername())) {
            LOG.debug("Username doesn't exist, returning 404");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            AuthenticationResponse response = authenticationService.authenticateUser(userDto);
            LOG.debug("Successful authentication, generated token is {}", response.getToken());
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            LOG.debug("Wrong password, returning 401");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
