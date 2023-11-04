package com.yoanpetrov.studentmanagementsystem;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO: 04-Nov-23 Change POST and DELETE requests to /users/{id}/courses and /courses/{id}/users to use a path variable and work like /users/{userId}/courses/{courseId} and /courses/{courseId}/users/{userId} respectively. This way, you don't have to pass the whole user or course in JSON but rather just its id and if it's not found in the database, 404 will be returned, which is a cleaner solution.
// TODO: 04-Nov-23 Change update handlers to user mappers instead of manually setting fields.
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
