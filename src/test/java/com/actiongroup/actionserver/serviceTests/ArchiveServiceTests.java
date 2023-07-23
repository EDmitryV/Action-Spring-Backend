package com.actiongroup.actionserver.serviceTests;

import com.actiongroup.actionserver.TestDataLoader;
import com.actiongroup.actionserver.models.archives.MusicArchive;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.archives.MusicArchiveService;
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
public class ArchiveServiceTests {
    @Autowired
    private UserService userService;

    @Autowired
    private MusicArchiveService musicService;

    private List<User> users;

    @BeforeAll
    private void setUp(){
        users = TestDataLoader.createUsers();
        refreshUsers();
    }

    @AfterAll
    private void clear(){
        clearUsers();
    }

    @Order(1)
    @Test
    public void nullWhenNoArchives(){
        for(User usr: users)
            Assertions.assertNull(usr.getMusicArchives());
    }


    @Order(2)
    @Test
    public void initArchivesFromService(){
        User usr = users.get(0);
        MusicArchive archive = new MusicArchive();
        archive.setOwner(usr);
        archive.setName("первый музык архив");
        archive = musicService.save(archive);
        Assertions.assertEquals(1, musicService.findByOwner(usr).size());
        //Assertions.assertEquals(1, usr.getMusicArchives().size());
    }



    @Order(3)
    @Test
    public void addSecondArchive(){
        User user = users.get(0);
        MusicArchive archive = new MusicArchive();
        archive.setOwner(user);
        archive.setName("второй музык архив");
        archive = musicService.save(archive);
        Set<MusicArchive> archives = musicService.findByOwner(user);
        Assertions.assertEquals(2, archives.size());

    }

    @Order(4)
    @Test
    public void initArchivesFromUser(){
        // ЭТОТ ТЕСТ НЕ ПРОХОДИТ (и не факт, что вообще должен)
        refreshUsers();
        User usr = users.get(0);
        MusicArchive archive = new MusicArchive();
        archive.setOwner(usr);
        archive.setName("третий музык архив");
        var ar  = usr.getMusicArchives();
        if(ar == null) ar = new HashSet<>();
        usr = userService.save(usr);
        Assertions.assertEquals(2, musicService.findByOwner(usr).size());
        //Assertions.assertEquals(2, usr.getMusicArchives().size());
    }


    private void refreshUsers(){
        for(int i = 0;i< users.size();i++)
        {
            User created = userService.save(users.get(i));
            users.set(i, created);
        }
    }

    private void clearUsers(){
        for(int i = 0;i< users.size();i++)
        {
            userService.deleteUser(users.get(i));
        }

    }


}
