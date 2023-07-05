package com.actiongroup.actionserver.models.stats;

import jakarta.persistence.*;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.models.events.Event;

@Entity
public class Review{

    @Id
    @GeneratedValue
    private long id;

    @OneToOne()
    @JoinColumn(name="author_id", referencedColumnName = "id")
    private User author;


    @OneToOne()
    @JoinColumn(name="event_id", referencedColumnName = "id")
    private Event event;

    private int rating;

    private String content;
    
}

