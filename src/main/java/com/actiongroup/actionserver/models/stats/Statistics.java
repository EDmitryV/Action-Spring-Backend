package com.actiongroup.actionserver.models.stats;

import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.models.events.Event;

import jakarta.persistence.*;


@Entity
public class Statistics {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne()
    @JoinColumn(name="author_id", referencedColumnName = "id")
    private User author;


    @OneToOne()
    @JoinColumn(name="event_id", referencedColumnName = "id")
    private Event event;
    

    private Float average;

    private Integer votersCount;
    
}




