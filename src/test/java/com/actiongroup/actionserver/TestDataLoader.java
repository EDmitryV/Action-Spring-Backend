package com.actiongroup.actionserver;

import com.actiongroup.actionserver.models.users.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TestDataLoader {
    public static List<User> createUsers(){
        List<User> users = new ArrayList<>();

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

        User Maria = new User();
        Maria.setBirthDate(LocalDate.parse("1995-02-25", formatter));
        Maria.setPassword("asdaswd");
        Maria.setEmail("Maria@masha.com");
        Maria.setUsername("Maria-tyan");


        User Artem = new User();
        Artem.setBirthDate(LocalDate.parse("2005-01-12", formatter));
        Artem.setPassword("dsdsddddd");
        Artem.setEmail("arrrrt@em.com");
        Artem.setUsername("artidodic");

        users.add(Egor);
        users.add(Sasha);
        users.add(Maria);
        users.add(Artem);

        return users;
    }
}
