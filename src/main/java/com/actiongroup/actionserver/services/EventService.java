package com.actiongroup.actionserver.services;

import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.repositories.events.EventRepository;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    @Autowired
    public EventService(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }

    public List<Event> getEventsOnMap(Point startPosition, Point endPosition, String dateTimeFilter, Boolean fromMe, Boolean fromSubscriptions, Boolean fromFriends, String[] filterCategories, User user){
        List<Double> authorsIds = new ArrayList<>();
        if (fromMe){
            //TODO add user_id to authorsIds
        }
        if (fromSubscriptions){
            //TODO add user_subscriptions to authorIds
        }
        if (fromFriends){
            //TODO add user_friends to authorIds
        }
        List<LocalDateTime> startDateTimes = new ArrayList<>();
        //TODO move available filters here?
//        if(dateTimeFilter == )
        //TODO check for min and max Y
        return eventRepository.findEventsOnMap(startPosition.getX(), startPosition.getY(), endPosition.getX(), endPosition.getY());
    }
}
