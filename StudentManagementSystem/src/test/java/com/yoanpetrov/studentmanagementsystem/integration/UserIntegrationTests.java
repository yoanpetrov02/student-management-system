package com.yoanpetrov.studentmanagementsystem.integration;

import com.yoanpetrov.studentmanagementsystem.entities.Course;
import com.yoanpetrov.studentmanagementsystem.entities.User;
import com.yoanpetrov.studentmanagementsystem.integration.config.TestConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@Import(TestConfig.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTests {

    private static final String BASE_URI = "api/v1/users";

    @Autowired
    private User testUser;
    @Autowired
    private User updatedUser;
    @Autowired
    private Course testCourse;

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
    void testCreateUser() {
        with().body(testUser)
            .when()
            .contentType(ContentType.JSON)
            .post(BASE_URI)
            .then()
            .assertThat()
            .statusCode(HttpStatus.CREATED.value());
    }

    @Order(2)
    @Test
    void testGetAllUsers() {
        User[] users = get(BASE_URI).then()
            .statusCode(HttpStatus.OK.value())
            .extract().as(User[].class);

        assertThat(users.length, equalTo(1));
        assertTrue(Arrays.stream(users).toList().contains(testUser));
    }

    @Order(3)
    @Test
    void testGetUserById() {
        with().get(BASE_URI + "/1").then()
            .assertThat()
            .body(
                "userId", equalTo(1),
                "firstName", equalTo("Test"),
                "lastName", equalTo("User"),
                "email", equalTo("test@test.com"));
    }

    @Order(4)
    @Test
    void testUpdateUser() {
        with().body(updatedUser).when()
            .contentType(ContentType.JSON)
            .put(BASE_URI + "/1").then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .and()
            .body("lastName", equalTo("Updated"));
    }

    @Order(5)
    @Test
    void testAddCourseToUser() {
        with().body(testCourse)
            .contentType(ContentType.JSON)
            .post("api/v1/courses").then()
            .assertThat()
            .statusCode(HttpStatus.CREATED.value());
        with().body(testCourse)
            .contentType(ContentType.JSON)
            .post(BASE_URI + "/1/courses").then()
            .assertThat()
            .statusCode(HttpStatus.CREATED.value());
        testCourse.setNumberOfStudents(testCourse.getNumberOfStudents() + 1);
    }

    @Order(6)
    @Test
    void testGetAllUserCourses() {
        Course[] courses = get(BASE_URI + "/1/courses").then()
            .statusCode(HttpStatus.OK.value())
            .extract().as(Course[].class);

        assertThat(courses.length, equalTo(1));
        assertTrue(Arrays.stream(courses).toList().contains(testCourse));
    }

    @Order(7)
    @Test
    void testRemoveCourseFromUser() {
        with().body(testCourse)
            .contentType(ContentType.JSON)
            .delete(BASE_URI + "/1/courses").then()
            .assertThat()
            .statusCode(HttpStatus.OK.value());
        given().get(BASE_URI + "/1/courses").then()
            .assertThat()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Order(8)
    @Test
    void testRemoveUserById() {
        testUser.setUserId(2L);
        with().body(testUser)
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
    void testRemoveAllUsers() {
        given().delete(BASE_URI).then()
            .assertThat()
            .statusCode(HttpStatus.OK.value());
        given().get(BASE_URI).then()
            .assertThat()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
