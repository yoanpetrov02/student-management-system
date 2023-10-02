package com.yoanpetrov.studentmanagementsystem;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO: 01-Oct-23 Add integration tests for the API.

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        BasicConfigurator.configure();
        SpringApplication.run(Main.class, args);
    }
}
