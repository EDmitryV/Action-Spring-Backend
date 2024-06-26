package com.actiongroup.actionserver.models.archives;


import com.actiongroup.actionserver.models.archives.media.Image;

import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.users.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Data
@MappedSuperclass
public abstract class Archive {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_event_id", referencedColumnName = "id")
    private Event mainEvent;


    @ManyToOne(fetch = FetchType.LAZY)
    private Image cover;

    private String name;
    private long contentCount = 0;

}