package com.actiongroup.actionserver.serviceTests;

import com.actiongroup.actionserver.TestDataLoader;
import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.events.EventPath;
import com.actiongroup.actionserver.models.events.PathCheckpoint;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.events.EventPathService;
import com.actiongroup.actionserver.services.events.EventService;
import com.actiongroup.actionserver.services.users.RelationshipService;
import com.actiongroup.actionserver.services.users.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class EventPathTests {

    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    private EventPathService eventPathService;

    private List<Event> events;
    private List<User> users;
    private EventPath path;

    @BeforeAll
    public void setUp() {
        initEvents();
        //initUsers();
        initPath();
    }

    @AfterAll
    public void clear(){
        clearPath();
        clearEvents();
        //clearUsers();
    }

    @Order(1)
    @Test
    public void getCheckPointsByPath(){
        List<PathCheckpoint> checkpoints = eventPathService.findCheckpointsByEventPath(path);
        Assertions.assertNotNull(checkpoints);
        Assertions.assertEquals(2, checkpoints.size());
        for(int i =0; i<checkpoints.size();i++){
            Assertions.assertEquals(i, checkpoints.get(i).getIndex());
        }
    }

    @Order(2)
    @Test
    public void getEventsByPath(){
        List<Event> eventssss = eventPathService.getEventsInPath(path);
        Assertions.assertNotNull(eventssss);
        Assertions.assertEquals(2, eventssss.size());
        Assertions.assertEquals("Встреча ребяток", eventssss.get(0).getName());
        Assertions.assertEquals("Аниме конвент", eventssss.get(1).getName());
    }


    @Order(3)
    @Test
    public void addToEndWorks(){
        eventPathService.addEventToPath(events.get(2), path);

        List<Event> eventssss = eventPathService.getEventsInPath(path);
        Assertions.assertNotNull(eventssss);
        Assertions.assertEquals(3, eventssss.size());
        Assertions.assertEquals("Встреча ребяток", eventssss.get(0).getName());
        Assertions.assertEquals("Аниме конвент", eventssss.get(1).getName());
        Assertions.assertEquals("Сходка попещиков anime down 69", eventssss.get(2).getName());
    }

    @Order(4)
    @Test
    public void addToCenterWorks(){
        eventPathService.addEventToPath(events.get(3), 1 ,path);

        List<Event> eventssss = eventPathService.getEventsInPath(path);
        Assertions.assertNotNull(eventssss);
        Assertions.assertEquals(4, eventssss.size());
        Assertions.assertEquals("Встреча ребяток", eventssss.get(0).getName());
        Assertions.assertEquals("Встреча для похода за пивом", eventssss.get(1).getName());
        Assertions.assertEquals("Аниме конвент", eventssss.get(2).getName());
        Assertions.assertEquals("Сходка попещиков anime down 69", eventssss.get(3).getName());
    }















    private void initEvents(){
        System.out.println("СОЗДАНИЕ ИВЕНТОВ");
        events = TestDataLoader.createEvents();
        for(int i =0; i< events.size();i++){
            Event savedEv = eventService.saveEvent(events.get(i));
            events.set(i, savedEv);
        }
    }

    private void clearEvents(){
        System.out.println("УДАЛЕНИЕ ИВЕНТОВ");
        for(int i =0; i< events.size();i++){
            eventService.deleteEvent(events.get(i));
        }
    }

    private void initUsers(){
        users = TestDataLoader.createUsers();
        for(int i =0; i< events.size();i++){
            User savedUser = userService.save(users.get(i));
            users.set(i, savedUser);
        }

    }

    private void clearUsers(){
        for(int i =0; i< users.size();i++){
            userService.deleteUser(users.get(i));
        }

    }

    private void initPath(){
        System.out.println("СОЗДАНИЕ ПУТЕЙ");
        path = eventPathService.saveEventPath(new EventPath());
        eventPathService.addEventToPath(events.get(0), path);
        eventPathService.addEventToPath(events.get(1), path);
    }

    private void clearPath(){
        System.out.println("УДАЛЕНИЕ ПУТЕЙ");
        eventPathService.deletePath(path);
    }

}