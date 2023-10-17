package com.yoanpetrov.studentmanagementsystem.rest.controllers;

import com.yoanpetrov.studentmanagementsystem.rest.dto.UserAccountDto;
import com.yoanpetrov.studentmanagementsystem.security.AuthenticationResponse;
import com.yoanpetrov.studentmanagementsystem.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/login")
public class LoginController {

    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody UserAccountDto userDto) {
        if (!authenticationService.checkUserExistence(userDto.getUsername())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            return ResponseEntity.ok(authenticationService.authenticateUser(userDto));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
