package com.yoanpetrov.studentmanagementsystem.rest.controllers;

import com.yoanpetrov.studentmanagementsystem.entities.UserAccount;
import com.yoanpetrov.studentmanagementsystem.rest.dto.UserAccountDto;
import com.yoanpetrov.studentmanagementsystem.security.AuthenticationResponse;
import com.yoanpetrov.studentmanagementsystem.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/v1/register")
public class RegisterController {

    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody UserAccountDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();

        if (authenticationService.checkUserExistence(username)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        AuthenticationResponse response = authenticationService.registerAccount(userDto);
        return ResponseEntity.ok(response);
    }
}
