package com.yoanpetrov.studentmanagementsystem.integration;

import com.yoanpetrov.studentmanagementsystem.model.Course;
import com.yoanpetrov.studentmanagementsystem.model.User;
import com.yoanpetrov.studentmanagementsystem.security.Role;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTests {

    private static final String BASE_URI = "api/v1/users";

    private static final User TEST_USER = User.builder() // TODO: 03-Oct-23 Put these in a Context and autowire them
        .userId(1L)
        .firstName("Test")
        .lastName("User")
        .email("test@test.com").build();

    private static final User UPDATED_USER = User.builder()
        .userId(1L)
        .firstName("Test")
        .lastName("Updated")
        .email("test@test.com").build();

    private static final Course TEST_COURSE = Course.builder()
        .courseId(1L)
        .name("Test")
        .description("Test description")
        .maxCapacity(10)
        .numberOfStudents(0).build();

    @LocalServerPort
    private int port;

    @BeforeAll
    public static void setupLogger() {
        BasicConfigurator.configure();
    }

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
    }

    @Order(1)
    @Test
    public void testCreateUser() {
        with().body(TEST_USER)
            .when()
            .contentType(ContentType.JSON)
            .post(BASE_URI)
            .then()
            .assertThat()
            .statusCode(HttpStatus.CREATED.value());
    }

    @Order(2)
    @Test
    public void testGetAllUsers() {
        User[] users = get(BASE_URI).then()
            .statusCode(HttpStatus.OK.value())
            .extract().as(User[].class);

        assertThat(users.length, equalTo(1));
        assertTrue(Arrays.stream(users).toList().contains(TEST_USER));
    }

    @Order(3)
    @Test
    public void testGetUserById() {
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
    public void testUpdateUser() {
        with().body(UPDATED_USER).when()
            .contentType(ContentType.JSON)
            .put(BASE_URI + "/1").then()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .and()
            .body("lastName", equalTo("Updated"));
    }

    @Order(5)
    @Test
    public void testAddCourseToUser() {
        with().body(TEST_COURSE)
            .contentType(ContentType.JSON)
            .post("api/v1/courses").then()
            .assertThat()
            .statusCode(HttpStatus.CREATED.value());
        with().body(TEST_COURSE)
            .contentType(ContentType.JSON)
            .post(BASE_URI + "/1/courses").then()
            .assertThat()
            .statusCode(HttpStatus.CREATED.value());
        TEST_COURSE.setNumberOfStudents(TEST_COURSE.getNumberOfStudents() + 1);
    }

    @Order(6)
    @Test
    public void testGetAllUserCourses() {
        Course[] courses = get(BASE_URI + "/1/courses").then()
            .statusCode(HttpStatus.OK.value())
            .extract().as(Course[].class);

        assertThat(courses.length, equalTo(1));
        assertTrue(Arrays.stream(courses).toList().contains(TEST_COURSE));
    }

    @Order(7)
    @Test
    public void testRemoveCourseFromUser() {
        with().body(TEST_COURSE)
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
    public void testRemoveUserById() {
        TEST_USER.setUserId(2L);
        with().body(TEST_USER)
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
    public void testRemoveAllUsers() {
        given().delete(BASE_URI).then()
            .assertThat()
            .statusCode(HttpStatus.OK.value());
        given().get(BASE_URI).then()
            .assertThat()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
