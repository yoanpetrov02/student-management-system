package com.yoanpetrov.studentmanagementsystem;

import com.yoanpetrov.studentmanagementsystem.model.User;
import com.yoanpetrov.studentmanagementsystem.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        /*UserService service = new UserService();
        User user = new User();
        user.setRole("admin");
        user.setFirstName("Yoan");
        user.setLastName("Petrov");
        user.setPassword("123123j123ljk12l");
        service.createUser(new User());*/
    }
}
