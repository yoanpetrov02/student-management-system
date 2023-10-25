package com.yoanpetrov.studentmanagementsystem.rest.controllers;

import com.yoanpetrov.studentmanagementsystem.rest.dto.UserAccountDto;
import com.yoanpetrov.studentmanagementsystem.security.AuthenticationResponse;
import com.yoanpetrov.studentmanagementsystem.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
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
        if (authenticationService.checkUserExistence(userDto.getUsername())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        AuthenticationResponse response = authenticationService.registerAccount(userDto);
        return ResponseEntity.ok(response);
    }
}
