package com.actiongroup.actionserver.models.events;

import com.actiongroup.actionserver.models.users.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
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


//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "checkpoint_id",referencedColumnName = "id")
//    private List<PathCheckpoint> checkpoints;

    @ManyToMany
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Set<User> users;
    private boolean publicEditable;


    public PathCheckpoint eventToCheckpoint(Event event){
        PathCheckpoint checkpoint = new PathCheckpoint();
        checkpoint.setEvent(event);
        checkpoint.setParentPath(this);
        return checkpoint;
    };

}
