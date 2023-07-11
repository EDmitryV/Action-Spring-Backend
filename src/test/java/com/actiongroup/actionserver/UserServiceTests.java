package com.actiongroup.actionserver;


import com.actiongroup.actionserver.models.users.Role;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.repositories.users.RoleRepository;
import com.actiongroup.actionserver.services.users.UserService;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public  class UserServiceTests {
    @Autowired
    private UserService userService;
    //@Autowired
    //private RoleRepository roleRepository;
    private List<User> users;

    @BeforeAll
    public void setUpUsers(){
        System.out.println("ЗАПУЩЕНА ИНИЦИАЛИЗАЦИЯ");
        users = TestDataLoader.createUsers();
        System.out.println("ЗАКОНЧЕНА ИНИЦИАЛИЗАЦИЯ");
    }

    @Order(1)
    @Test
    public void saveUsersWorksCorrect(){
        System.out.println("ЗАПУЩЕНО СОХРАНЕНИЕ");
        for(User user:users){
            if(userService.existsByUsername(user.getUsername()))
                Assertions.assertNull(userService.save(user));
            else
                Assertions.assertNotNull(userService.save(user));
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
            User foundUser = userService.findByEmail(user.getEmail()).get();
            Assertions.assertEquals(user.getUsername(), foundUser.getUsername());
            Assertions.assertEquals(user.getEmail(), foundUser.getEmail());
        }
        User johnDoe  = new User();
        Assertions.assertNull(johnDoe.getUsername());
        Assertions.assertNull(johnDoe.getEmail());
    }



//    @Order(4)
//    @Test @Disabled
//    public void userRolesAdded(){
//        System.out.println("ПРОВЕРКА РОЛЕЙ");
//        for(User user:users){
//            User foundUser = userService.findByUsername(user.getUsername());
//            Role role = roleRepository.findByName("ROLE_USER").get();
//            Assertions.assertTrue(foundUser.getRoles().contains(role));
//        }
//    }

    @Order(4)
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
