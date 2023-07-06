package com.actiongroup.actionserver.models.events;

import com.actiongroup.actionserver.models.users.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity
public class EventPath {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "author_id",referencedColumnName = "id")
    private User author;


    @ManyToMany
    @JoinColumn(name = "event_id",referencedColumnName = "id")
    private List<Event> events;

    @ManyToMany
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Set<User> users;
    private boolean publicEditable;

}
