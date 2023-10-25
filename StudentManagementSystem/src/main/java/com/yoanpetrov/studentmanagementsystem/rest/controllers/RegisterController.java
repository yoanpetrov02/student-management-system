package com.yoanpetrov.studentmanagementsystem.rest.controllers;

import com.yoanpetrov.studentmanagementsystem.rest.dto.UserAccountDto;
import com.yoanpetrov.studentmanagementsystem.security.AuthenticationResponse;
import com.yoanpetrov.studentmanagementsystem.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Rest controller for the register endpoints.
 */
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/v1/register")
public class RegisterController {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterController.class);

    private final AuthenticationService authenticationService;

    /**
     * Registers the given user.
     *
     * @param userDto the credentials of the user's account.
     * @return 200 and a JWT token if the user was successfully registered,
     * 409 if a user account with this username already exists.
     */
    @PostMapping
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody UserAccountDto userDto) {
        LOG.debug("Attempting to register user account");
        if (authenticationService.checkUserExistence(userDto.getUsername())) {
            LOG.debug("Account with the same username already exists, returning 409");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        AuthenticationResponse response = authenticationService.registerAccount(userDto);
        LOG.debug("Successful registration, generated token is {}", response.getToken());
        return ResponseEntity.ok(response);
    }
}
