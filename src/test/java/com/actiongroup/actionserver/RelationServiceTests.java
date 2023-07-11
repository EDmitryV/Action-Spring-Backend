package com.actiongroup.actionserver;

import com.actiongroup.actionserver.models.users.Role;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.models.users.UserRelation;
import com.actiongroup.actionserver.repositories.users.RoleRepository;
import com.actiongroup.actionserver.services.users.RelationshipService;
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
public class RelationServiceTests {
    @Autowired
    private UserService userService;
    @Autowired
    private RelationshipService relationshipService;
    private List<User> users;

    @BeforeAll
    public void setUpUsers(){
        System.out.println("ЗАПУЩЕНА ИНИЦИАЛИЗАЦИЯ");
        users = TestDataLoader.createUsers();
        System.out.println("ЗАКОНЧЕНА ИНИЦИАЛИЗАЦИЯ");
        System.out.println("ЗАПУЩЕНО СОХРАНЕНИЕ");
        for(int i = 0; i<users.size() ;i++) {
            User foundUsr = userService.findByUsername(users.get(i).getUsername());

            if(foundUsr != null) users.set(i, foundUsr);
            else userService.save(users.get(i));
        }
        System.out.println("ЗАКОНЧЕНО СОХРАНЕНИЕ");
        System.out.println("СОЗДАНИЕ ПОДПИСКИ");
        subscribeInit();
        System.out.println("ПОДПИСКИ УСТАНОВЛЕНЫ");
    }

    private void subscribeInit(){

    }

    @Order(1)
    @Test
    public void subscribtionCreationWorks(){
        User usr = users.get(0);
        UserRelation sub1 = relationshipService.subscribe(usr, users.get(1));
        Assertions.assertNotNull(sub1);
        Assertions.assertEquals(usr.getUsername(), sub1.getSourceUser().getUsername());
        Assertions.assertEquals(users.get(1).getUsername(), sub1.getTargetUser().getUsername());
        Assertions.assertEquals(UserRelation.RelationTypes.Subscription, sub1.getRelationType());


        UserRelation sub2 =relationshipService.subscribe(usr, users.get(2));
        Assertions.assertNotNull(sub2);
        Assertions.assertEquals(usr.getUsername(), sub2.getSourceUser().getUsername());
        Assertions.assertEquals(users.get(1).getUsername(), sub1.getTargetUser().getUsername());
        Assertions.assertEquals(UserRelation.RelationTypes.Subscription, sub2.getRelationType());


        UserRelation sub3 = relationshipService.subscribe(users.get(1), usr);
        Assertions.assertNotNull(sub3);
        Assertions.assertEquals(users.get(1).getUsername(), sub3.getSourceUser().getUsername());
        Assertions.assertEquals(usr.getUsername(), sub3.getTargetUser().getUsername());
        Assertions.assertEquals(UserRelation.RelationTypes.Subscription, sub3.getRelationType());
    }

    @Order(2)
    @Test
    public void getSubscribtionsWorks() {
        System.out.println("ПРОВЕРКА ПОДПИСОК");
        User usr = users.get(0);
        Set<User> subs = relationshipService.getSubscriptions(usr);
        Assertions.assertTrue(subs.size()==2);

        Set<String> unames = new HashSet<>();
        subs.forEach(sub->{unames.add(sub.getUsername());});
        Assertions.assertTrue(unames.contains(users.get(1).getUsername()));
        Assertions.assertTrue(unames.contains(users.get(2).getUsername()));
    }

    @Order(3)
    @Test
    public void subscribeTwiceCausesNoNewRows(){
        relationshipService.subscribe(users.get(0), users.get(1));
        Set<User> subs = relationshipService.getSubscriptions(users.get(0));
        Assertions.assertEquals(2, subs.size());

    }

    @Order(4)
    @Test
    public void cannotSubscribeOnYorself(){
        relationshipService.subscribe(users.get(0), users.get(0));
        Set<User> subs = relationshipService.getSubscriptions(users.get(0));
        Assertions.assertEquals(2, subs.size());
    }


    @Order(5)
    @Test
    public void unsubscribeCheck() {
        relationshipService.unsubscribe(users.get(0), users.get(2));
        Assertions.assertTrue(true);
    }


    @Order(6)
    @Test
    public void friendshipsCheck(){
        relationshipService.subscribe(users.get(2), users.get(3));
        relationshipService.subscribe(users.get(3), users.get(2));
        relationshipService.subscribe(users.get(3), users.get(1));
        relationshipService.subscribe(users.get(2), users.get(0));

        Set<User> friendsOf2 = relationshipService.getFriends(users.get(2));
        Set<User> friendsOf3 = relationshipService.getFriends(users.get(3));

        Assertions.assertEquals(1, friendsOf2.size());
        Assertions.assertEquals(1, friendsOf3.size());
    }


    @Order(7)
    @Test
    public void addingToBlackListDestroysFriendship(){
        relationshipService.block(users.get(2), users.get(0));
        relationshipService.block(users.get(2), users.get(3));


        Set<User> friends = relationshipService.getFriends(users.get(2));
        Assertions.assertEquals(0, friends.size());
        Set<User> subscribtions = relationshipService.getSubscriptions(users.get(2));
        Assertions.assertEquals(0, subscribtions.size());
        Set<User> blacklist = relationshipService.getBlacklist(users.get(2));
        Assertions.assertEquals(2, blacklist.size());
    }



    @Order(8)
    @Test
    public void removeFromBlackList(){
        relationshipService.removeFromBlackList(users.get(2),users.get(0));
        relationshipService.removeFromBlackList(users.get(2),users.get(3));
        Set<User> blacklist = relationshipService.getFriends(users.get(2));
        Assertions.assertEquals(0, blacklist.size());
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
