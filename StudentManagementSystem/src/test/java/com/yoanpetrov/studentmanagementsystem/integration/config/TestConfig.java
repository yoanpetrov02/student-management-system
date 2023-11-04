package com.yoanpetrov.studentmanagementsystem.integration.config;

import com.yoanpetrov.studentmanagementsystem.entities.Course;
import com.yoanpetrov.studentmanagementsystem.entities.User;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

/**
 * Disables security for the tests.
 */
@Profile("test")
@TestConfiguration
public class TestConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().anyRequest();
    }

    @Bean
    public User testUser() {
        return User.builder()
            .userId(1L)
            .firstName("Test")
            .lastName("User")
            .email("test@test.com").build();
    }

    @Bean
    public Course testCourse() {
        return Course.builder()
            .courseId(1L)
            .name("Test")
            .description("Test description")
            .maxCapacity(10)
            .numberOfStudents(0).build();
    }

    @Bean
    public User updatedUser() {
        return User.builder()
            .userId(1L)
            .firstName("Test")
            .lastName("Updated")
            .email("test@test.com").build();
    }

    @Bean
    public Course updatedCourse() {
        return Course.builder()
            .courseId(1L)
            .name("Test")
            .description("Updated test description")
            .maxCapacity(10)
            .numberOfStudents(0).build();
    }
}
