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
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfig.class)
public class CourseIntegrationTests {

    private static final String BASE_URI = "api/v1/courses";

    @Autowired
    private Course testCourse;
    @Autowired
    private Course updatedCourse;
    @Autowired
    private User testUser;

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
        with().body(testCourse)
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
        assertTrue(Arrays.stream(courses).toList().contains(testCourse));
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
        with().body(updatedCourse).when()
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
        with().body(testUser)
            .contentType(ContentType.JSON)
            .post("api/v1/users").then()
            .assertThat()
            .statusCode(HttpStatus.CREATED.value());
        with().body(testUser)
            .contentType(ContentType.JSON)
            .post(BASE_URI + "/1/users").then()
            .assertThat()
            .statusCode(HttpStatus.CREATED.value());
        testCourse.setNumberOfStudents(testCourse.getNumberOfStudents() + 1);
    }

    @Order(6)
    @Test
    void testGetAllCourseUsers() {
        User[] users = get(BASE_URI + "/1/users").then()
            .statusCode(HttpStatus.OK.value())
            .extract().as(User[].class);

        assertThat(users.length, equalTo(1));
        assertTrue(Arrays.stream(users).toList().contains(testUser));
    }

    @Order(7)
    @Test
    void testRemoveUserFromCourse() {
        with().body(testUser)
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
        testCourse.setCourseId(2L);
        with().body(testCourse)
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
