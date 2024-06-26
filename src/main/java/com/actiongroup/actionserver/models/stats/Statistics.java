package com.actiongroup.actionserver.models.stats;

import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.models.events.Event;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Statistics {

    @Id
    @GeneratedValue
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="event_id")
    private Event event;

    private Float average;

    private Integer votersCount;

    private Integer visitorsCount;
}




