package com.actiongroup.actionserver;


import com.actiongroup.actionserver.models.users.Role;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.repositories.user.RoleRepository;
import com.actiongroup.actionserver.services.users.UserService;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public  class UserServiceTests {

    private Integer TestNumber;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    private List<User> users;

    @BeforeAll
    public void setUpUsers(){
        System.out.println("ЗАПУЩЕНА ИНИЦИАЛЬЗАЦИЯ");
        users = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        User Egor = new User();
        Egor.setBirthDate(LocalDate.parse("1970-11-10", formatter));
        Egor.setPassword("123");
        Egor.setEmail("egor@egor.com");
        Egor.setUsername("egorka");


        User Sasha = new User();
        Sasha.setBirthDate(LocalDate.parse("2000-05-18", formatter));
        Sasha.setPassword("123455");
        Sasha.setEmail("Sasha@Sasha.com");
        Sasha.setUsername("sashka-sann");


        users.add(Egor);
        users.add(Sasha);


    }

    @Order(1)
    @Test
    public void saveUsersWorksCorrect(){
        System.out.println("ЗАПУЩЕНО СОХРАНЕНИЕ");
        for(User user:users){
            if(userService.existsByUsername(user.getUsername()))
                Assertions.assertFalse(userService.save(user));
            else
                Assertions.assertTrue(userService.save(user));
        }
    }



    @Order(2)
    @Test
    public void findByUsername(){
        System.out.println("ПОИСК ПО ИМЕНИ ПОЛЬЗОВАТЕЛЯ");
        for(User user:users){
            User foundUser = userService.findByUsername(user.getUsername());
            System.out.println(foundUser.getUsername());



            Assertions.assertEquals(user.getUsername(), foundUser.getUsername());
            Assertions.assertEquals(user.getEmail(), foundUser.getEmail());
        }

        User johnDoe  = new User();
        Assertions.assertNull(johnDoe.getUsername());
        Assertions.assertNull(johnDoe.getEmail());
    }


    @Order(3)
    @Test
    public void findByEmail(){
        System.out.println("ПОИСК ПО ПОЧТЕ");
        for(User user:users){
            User foundUser = userService.findByEmail(user.getEmail());
            Assertions.assertEquals(user.getUsername(), foundUser.getUsername());
            Assertions.assertEquals(user.getEmail(), foundUser.getEmail());
        }

        User johnDoe  = new User();
        Assertions.assertNull(johnDoe.getUsername());
        Assertions.assertNull(johnDoe.getEmail());
    }



    @Order(4)
    @Test
    public void userRolesAdded(){
        System.out.println("ПРОВЕРКА РОЛЕЙ");
        for(User user:users){
            User foundUser = userService.findByUsername(user.getUsername());
            Role role = roleRepository.findByName("ROLE_USER").get();
            Assertions.assertTrue(foundUser.getRoles().contains(role));
        }
    }

    @Order(5)
    @Test
    public void everyUserHasSettings(){
        System.out.println("ПРОВЕРКА НАСТРОЕК");
        for(User user:users){
            Assertions.assertNotNull(userService.getSettingsByUser(user));
        }

    }

    @AfterAll
    public void clearTestUsers(){

        System.out.println("УДАЛЕНИЕ ВСЕГО");
        for(User user:users)
        {
            userService.deleteUser(user);
            Assertions.assertFalse(userService.existsByUsername(user.getUsername()));
        }
    }
}
