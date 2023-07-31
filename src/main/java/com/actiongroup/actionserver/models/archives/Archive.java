package com.actiongroup.actionserver.models.archives;

import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.users.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Data
@MappedSuperclass
public abstract class Archive {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @ManyToOne()
    @JoinColumn(name = "main_event_id", referencedColumnName = "id")
    private Event mainEvent;

    private String name;
    private int contentCount;
    @RequiredArgsConstructor
    public enum Type{
        Audio("audio"),
        Video("video"),
        Event("event"),
        Image("image");
        @Getter
        private final String mediaType;
    }

}