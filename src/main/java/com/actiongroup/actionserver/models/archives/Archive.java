package com.actiongroup.actionserver.models.archives;

import com.actiongroup.actionserver.models.archives.media.Image;
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

    @ManyToOne()
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @ManyToOne()
    @JoinColumn(name = "main_event_id", referencedColumnName = "id")
    private Event mainEvent;

    @ManyToOne()
    private Image cover;

    private String name;
    private long contentCount = 0;

    @RequiredArgsConstructor
    public enum Type {
        Audio("audio"),
        Video("video"),
        Event("event"),
        Image("image");
        @Getter
        private final String mediaType;
    }

}