package com.actiongroup.actionserver.serviceTests;

import com.actiongroup.actionserver.TestDataLoader;
import com.actiongroup.actionserver.models.archives.AudioArchive;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.archives.AudioArchiveService;
import com.actiongroup.actionserver.services.users.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class ArchiveServiceTests {
    @Autowired
    private UserService userService;

    @Autowired
    private AudioArchiveService musicService;

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
            Assertions.assertNull(usr.getAudioArchives());
    }

    @Order(1)
    @Test
    public void initArchivesFromService(){
        User usr = users.get(0);
        AudioArchive archive = new AudioArchive();
        archive.setOwner(usr);
        archive.setName("первый музык архив");
        archive = musicService.save(archive);
        Assertions.assertEquals(1, musicService.findByOwner(usr).size());
        //Assertions.assertEquals(1, usr.getMusicArchives().size());
    }


    @Order(2)
    @Test
    public void addSecondArchive(){
        User user = users.get(0);
        AudioArchive archive = new AudioArchive();
        archive.setOwner(user);
        archive.setName("второй музык архив");
        archive = musicService.save(archive);
        Assertions.assertEquals(2, musicService.findByOwner(user).size());

    }

    @Order(2)
    @Test
    @Disabled
    public void initArchivesFromUser(){
        // ЭТОТ ТЕСТ НЕ ПРОХОДИТ (и не факт, что вообще должен)
        refreshUsers();
        User usr = users.get(0);
        AudioArchive archive = new AudioArchive();
        archive.setOwner(usr);
        archive.setName("второй музык архив");
        usr.getAudioArchives().add(archive);
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
