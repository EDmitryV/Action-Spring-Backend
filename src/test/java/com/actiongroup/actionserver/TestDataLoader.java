package com.actiongroup.actionserver;


import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.events.Tag;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.users.UserService;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    public static List<Tag> createTagTree(){
        Tag sport = new Tag();
        sport.setName("спорт");
        //--------SPORTS----------------
        Tag water = new Tag();
        water.setName("водный");
        water.setParentTag(sport);

        Tag air = new Tag();
        air.setName("воздушный");
        air.setParentTag(sport);

        Tag ice = new Tag();
        ice.setName("зимний");
        ice.setParentTag(sport);

        //-----ICE SPORTS------------------

        Tag hockey = new Tag();
        hockey.setName("хоккей");
        hockey.setParentTag(ice);

        Tag biathlon = new Tag();
        biathlon.setName("биатлон");
        biathlon.setParentTag(ice);

        Tag figureSkating = new Tag();
        figureSkating.setName("фигурное катание");
        figureSkating.setParentTag(ice);
        //-------AIR SPORTS--------------------
        Tag parachuting = new Tag();
        parachuting.setName("парашютирование");
        parachuting.setParentTag(air);

        //--------WATER SPORTS---------------------
        Tag swimming = new Tag();
        swimming.setName("павание");
        swimming.setParentTag(water);

        Tag polo = new Tag();
        polo.setName("водное поло");
        polo.setParentTag(water);

        List<Tag> childrenTags = new ArrayList<>();
        childrenTags.add(polo);
        childrenTags.add(swimming);
        childrenTags.add(parachuting);
        childrenTags.add(figureSkating);
        childrenTags.add(biathlon);
        childrenTags.add(hockey);

        return childrenTags;
    }


    public static List<Event> createEvents(){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Event ev1 = new Event();
        ev1.setName("Встреча ребяток");
        ev1.setStartsAt(LocalDateTime.parse("2023-07-20 13:00:00", formatter));

        Event ev2 = new Event();
        ev2.setName("Аниме конвент");
        ev2.setStartsAt(LocalDateTime.parse("2023-11-01 09:00:00", formatter));

        Event ev3 = new Event();
        ev3.setName("Сходка попещиков anime down 69");
        ev3.setStartsAt(LocalDateTime.parse("2023-10-05 08:30:00", formatter));

        Event ev4 = new Event();
        ev4.setName("Встреча для похода за пивом");
        ev4.setStartsAt(LocalDateTime.parse("2023-07-20 16:50:00", formatter));


        return new ArrayList<>(List.of(ev1, ev2, ev3, ev4));
    }
}
