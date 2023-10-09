package com.yoanpetrov.studentmanagementsystem;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// TODO: 06-Oct-23 Finish integrating user_accounts into the project

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Main {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        SpringApplication.run(Main.class, args);
    }
}
