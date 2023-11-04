package com.yoanpetrov.studentmanagementsystem.integration;

import com.yoanpetrov.studentmanagementsystem.entities.Course;
import com.yoanpetrov.studentmanagementsystem.entities.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@WebMvcTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CourseIntegrationTests {

    private static final String BASE_URI = "api/v1/courses";

    private static final User TEST_USER = User.builder() // TODO: 03-Oct-23 Put these in a Context and autowire them
        .userId(1L)
        .firstName("Test")
        .lastName("User")
        .email("test@test.com").build();

    private static final Course UPDATED_COURSE = Course.builder()
        .courseId(1L)
        .name("Test")
        .description("Updated test description")
        .maxCapacity(10)
        .numberOfStudents(0).build();

    private static final Course TEST_COURSE = Course.builder()
        .courseId(1L)
        .name("Test")
        .description("Test description")
        .maxCapacity(10)
        .numberOfStudents(0).build();

    @LocalServerPort
    private int port;

    @BeforeAll
    static void setupLogger() {
        BasicConfigurator.configure(); // configures logger
    }

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Order(1)
    @Test
    void testCreateCourse() {
        with().body(TEST_COURSE)
            .when()
            .contentType(ContentType.JSON)
            .post(BASE_URI)
            .then()
            .assertThat()
            .statusCode(HttpStatus.CREATED.value());
    }

    @Order(2)
    @Test
    void testGetAllCourses() {
        Course[] courses = get(BASE_URI).then()
            .statusCode(HttpStatus.OK.value())
            .extract().as(Course[].class);

        assertThat(courses.length, equalTo(1));
        assertTrue(Arrays.stream(courses).toList().contains(TEST_COURSE));
    }

    @Order(3)
    @Test
    void testGetCourseById() {
        with().get(BASE_URI + "/1").then()
            .assertThat()
            .body(
                "courseId", equalTo(1),
                "name", equalTo("Test"),
                "description", equalTo("Test description"),
                "maxCapacity", equalTo(10),
                "numberOfStudents", equalTo(0));
    }

    @Order(4)
    @Test
    void testUpdateCourse() {
        with().body(UPDATED_COURSE).when()
            .contentType(ContentType.JSON)
            .put(BASE_URI + "/1").then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .and()
            .body("description", equalTo("Updated test description"));
    }

    @Order(5)
    @Test
    void testAddUserToCourse() {
        with().body(TEST_USER)
            .contentType(ContentType.JSON)
            .post("api/v1/users").then()
            .assertThat()
            .statusCode(HttpStatus.CREATED.value());
        with().body(TEST_USER)
            .contentType(ContentType.JSON)
            .post(BASE_URI + "/1/users").then()
            .assertThat()
            .statusCode(HttpStatus.CREATED.value());
        TEST_COURSE.setNumberOfStudents(TEST_COURSE.getNumberOfStudents() + 1);
    }

    @Order(6)
    @Test
    void testGetAllCourseUsers() {
        User[] users = get(BASE_URI + "/1/users").then()
            .statusCode(HttpStatus.OK.value())
            .extract().as(User[].class);

        assertThat(users.length, equalTo(1));
        assertTrue(Arrays.stream(users).toList().contains(TEST_USER));
    }

    @Order(7)
    @Test
    void testRemoveUserFromCourse() {
        with().body(TEST_USER)
            .contentType(ContentType.JSON)
            .delete(BASE_URI + "/1/users").then()
            .assertThat()
            .statusCode(HttpStatus.OK.value());
        given().get(BASE_URI + "/1/users").then()
            .assertThat()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Order(8)
    @Test
    void testRemoveCourseById() {
        TEST_COURSE.setCourseId(2L);
        with().body(TEST_COURSE)
            .contentType(ContentType.JSON)
            .post(BASE_URI).then()
            .assertThat()
            .statusCode(HttpStatus.CREATED.value());
        given().delete(BASE_URI + "/2").then()
            .assertThat()
            .statusCode(HttpStatus.OK.value());
    }

    @Order(9)
    @Test
    void testRemoveAllCourses() {
        given().delete(BASE_URI).then()
            .assertThat()
            .statusCode(HttpStatus.OK.value());
        given().get(BASE_URI).then()
            .assertThat()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
