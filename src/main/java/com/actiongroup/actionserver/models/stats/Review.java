package com.actiongroup.actionserver.models.stats;

import jakarta.persistence.*;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.models.events.Event;
import lombok.Data;

@Data
@Entity
public class Review{

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne()
    @JoinColumn(name="author_id", referencedColumnName = "id")
    private User author;


    @ManyToOne()
    @JoinColumn(name="event_id", referencedColumnName = "id")
    private Event event;

    private int rating;
    private String content;
}

