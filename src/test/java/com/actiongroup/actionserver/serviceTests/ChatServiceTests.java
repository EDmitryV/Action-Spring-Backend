package com.actiongroup.actionserver.serviceTests;


import com.actiongroup.actionserver.TestDataLoader;
import com.actiongroup.actionserver.models.chats.Chat;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.chats.ChatService;
import com.actiongroup.actionserver.services.users.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@Disabled
public class ChatServiceTests {

    private List<User> users;

    @Autowired
    private UserService userService;
    @Autowired
    private ChatService chatService;


    @Test
    @Order(1)
    public void chatCreation(){
        Chat chat = new Chat();
        Set<User> members = new HashSet<>();
        members.add(users.get(0));
        members.add(users.get(1));
        chat.setMembers(members);
        chat = chatService.saveChat(chat);


        Assertions.assertEquals(2,chat.getMembers().size());
        Assertions.assertEquals(1, users.get(0).getChats().size());
        Assertions.assertEquals(1, users.get(1).getChats().size());
    }



    private void initUsers(){
        users = TestDataLoader.createUsers();
        for(int i =0;i<users.size();i++){
            User found = userService.findByUsername(users.get(i).getUsername());
            if(found == null){
                users.set(i, userService.save(users.get(i)));
            }
            else{
                users.set(i, found);
            }
        }
    }

    private void clearUsers(){
        for(User u: users)
            userService.deleteUser(u);
    }

    @BeforeAll
    public void init(){
        initUsers();
    }

    @AfterAll
    public void clear(){
        clearUsers();
    }

}
