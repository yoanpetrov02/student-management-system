package com.yoanpetrov.studentmanagementsystem.controller;

import com.yoanpetrov.studentmanagementsystem.model.UserAccount;
import com.yoanpetrov.studentmanagementsystem.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/register")
public class RegisterController {

    private final AuthenticationService authenticationService;

    @PostMapping
    public String registerUser(@RequestBody Map<String, String> userData) {
        String username = userData.get("username");
        String password = userData.get("password");

        if (authenticationService.checkUserExistence(username)) {
            return "A user with that username already exists!";
        }
        UserAccount account = authenticationService.createUserAccount(username, password);
        return account.getUsername() + " " + password + " WAS CREATED";
    }
}
