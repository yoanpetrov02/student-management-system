package com.yoanpetrov.studentmanagementsystem.integration;

import com.yoanpetrov.studentmanagementsystem.entities.User;
import com.yoanpetrov.studentmanagementsystem.entities.UserAccount;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import(TestConfig.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountIntegrationTests {

    private static final String BASE_URI = "api/v1/accounts";

    @Autowired
    private UserAccount testUserAccount;
    @Autowired
    private UserAccount updatedUserAccount;
    @Autowired
    private UserAccount userAccountToDelete;
    @Autowired
    private User testUser;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
    void testCreateAccount() {
        with().body(testUserAccount)
            .when()
            .contentType(ContentType.JSON)
            .post(BASE_URI)
            .then()
            .assertThat()
            .statusCode(HttpStatus.CREATED.value());
    }

    @Order(2)
    @Test
    void testGetAllAccounts() {
        UserAccount[] accounts = get(BASE_URI).then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(UserAccount[].class);

        assertThat(accounts.length, equalTo(2));

        UserAccount account = accounts[1];
        assertThat(account.getAccountId(), equalTo(2L));
        assertThat(account.getUsername(), equalTo("test"));
        assertTrue(passwordEncoder.matches("test", account.getPassword()));
        assertThat(account.getRole().name(), equalTo("STUDENT"));
    }

    @Order(3)
    @Test
    void testGetAccountById() {
        UserAccount account = get(BASE_URI + "/2").then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(UserAccount.class);

        assertThat(account.getAccountId(), equalTo(2L));
        assertThat(account.getUsername(), equalTo("test"));
        assertTrue(passwordEncoder.matches("test", account.getPassword()));
        assertThat(account.getRole().name(), equalTo("STUDENT"));
    }

    @Order(4)
    @Test
    void testUpdateAccount() {
        UserAccount updated = with().body(updatedUserAccount).when()
            .contentType(ContentType.JSON)
            .put(BASE_URI + "/2")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .as(UserAccount.class);

        assertTrue(passwordEncoder.matches("updated", updated.getPassword()));
    }

    @Order(5)
    @Test
    void testSetAccountUser() {
        with().body(testUser)
            .contentType(ContentType.JSON)
            .post("api/v1/users").then()
            .assertThat()
            .statusCode(HttpStatus.CREATED.value());
        with().contentType(ContentType.JSON)
            .post(BASE_URI + "/2/user/1").then()
            .assertThat()
            .statusCode(HttpStatus.OK.value());
    }

    @Order(6)
    @Test
    void testDeleteAccountById() {
        testUserAccount.setAccountId(3L);
        with().body(userAccountToDelete)
            .contentType(ContentType.JSON)
            .post(BASE_URI).then()
            .assertThat()
            .statusCode(HttpStatus.CREATED.value());
        given().delete(BASE_URI + "/3").then()
            .assertThat()
            .statusCode(HttpStatus.OK.value());
    }

    @Order(7)
    @Test
    void testDeleteAllAccounts() {
        given().delete(BASE_URI).then()
            .assertThat()
            .statusCode(HttpStatus.OK.value());
        given().get(BASE_URI).then()
            .assertThat()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
