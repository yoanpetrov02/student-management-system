package com.yoanpetrov.studentmanagementsystem;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO: 19-Oct-23 Extract UserAccount and all other auth related things to another package
// TODO: 16-Oct-23 Add a test profile with a different configuration
// TODO: 16-Oct-23 Add JSON validation using Hibernate Validator

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
