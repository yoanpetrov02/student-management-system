package com.yoanpetrov.studentmanagementsystem;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// TODO: 19-Oct-23 Extract UserAccount and all other auth related things to another package
// TODO: 16-Oct-23 Add a test profile with a different configuration
// TODO: 16-Oct-23 Add JSON validation using Hibernate Validator

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        BasicConfigurator.configure(); // configures logger
        SpringApplication.run(Main.class, args);
    }
}
