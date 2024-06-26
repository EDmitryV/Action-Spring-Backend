package com.actiongroup.actionserver.APITests;


import com.actiongroup.actionserver.TestDataLoader;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.users.UserService;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import io.restassured.RestAssured;
import io.restassured.response.Response;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Disabled
public class UserControllerTests {
    private String baseUrl = "http://localhost:8080";

    @Autowired
    UserService userService;
    private List<User> users;

    @BeforeAll
    public void setUpUsers() {
        System.out.println("ЗАПУЩЕНА ИНИЦИАЛИЗАЦИЯ");
        users = TestDataLoader.createUsers();
        System.out.println("ЗАКОНЧЕНА ИНИЦИАЛИЗАЦИЯ");
    }


    @Test
    public void registerTest(){
        String basepath = "/auth/register";
        Response response = RestAssured.given()
                .baseUri(baseUrl)
                .basePath(basepath)
                .body("{\n" +
                        "    \"username\":\"test\",\n" +
                        "    \"email\":\"test@test.com\",\n" +
                        "    \"password\":\"test\"\n" +
                        "}")
                .when()
                .post()
                .then()
                .extract().response();

        Assertions.assertEquals(200, response.statusCode());
        // TODO дополнить тест проверкой какой-нибудь еще


    }

    @Test
    public void testGetUser()
    {
        User user = new User();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        user.setBirthDate(LocalDate.parse("1995-09-24", formatter));
        user.setPassword("1234567lkdf");
        user.setEmail("biba@baba.com");
        user.setUsername("Babich");


        User actualuser =  userService.save(user);
        Response response = RestAssured.given()
                .baseUri(baseUrl)
                .basePath("/" + actualuser.getId())
                .when()
                .get()
                .then()
                .extract().response();

        Assertions.assertEquals(200, response.statusCode());
        userService.deleteUser(actualuser);
    }


}
