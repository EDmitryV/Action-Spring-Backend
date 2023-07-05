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
    @JoinColumn(name="event_id", referencedColumnName = "id")
    private Event event;
    

    private Float average;

    private Integer votersCount;

    private Integer visitorsCount;

    public long getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Float getAverage() {
        return average;
    }

    public void setAverage(Float average) {
        this.average = average;
    }

    public Integer getVotersCount() {
        return votersCount;
    }

    public void setVotersCount(Integer votersCount) {
        this.votersCount = votersCount;
    }

    public Integer getVisitorsCount() {
        return visitorsCount;
    }

    public void setVisitorsCount(Integer visitorsCount) {
        this.visitorsCount = visitorsCount;
    }
}




