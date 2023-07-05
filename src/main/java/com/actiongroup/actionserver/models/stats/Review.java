package com.actiongroup.actionserver.models.stats;

import jakarta.persistence.*;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.models.events.Event;

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



    public long getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

