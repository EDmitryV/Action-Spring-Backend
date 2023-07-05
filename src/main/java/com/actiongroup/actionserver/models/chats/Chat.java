package com.actiongroup.actionserver.models.chats;

import com.actiongroup.actionserver.models.archives.media.Image;
import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.users.User;
import java.util.Set;
import jakarta.persistence.*;

@Entity
public class Chat{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private String name;

    @ManyToOne()
    @JoinColumn(name="image_id", referencedColumnName = "id")
    private Image icon;


    @OneToOne()
    @JoinColumn(name="event_id", referencedColumnName = "id")
    private Event event;


    @ManyToMany()
    @JoinColumn(name="member_id", referencedColumnName = "id")
    private Set<User> memers;


}