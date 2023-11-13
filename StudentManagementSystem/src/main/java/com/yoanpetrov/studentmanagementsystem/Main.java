package com.yoanpetrov.studentmanagementsystem;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO: 04-Nov-23 Write integration tests for AccountController.

/**
 * Main starting point of the application.
 */
@SpringBootApplication
public class Main {

    /**
     * Configures the loggers and runs the application.
     */
    public static void main(String[] args) {
        BasicConfigurator.configure();
        SpringApplication.run(Main.class, args);
    }
}
