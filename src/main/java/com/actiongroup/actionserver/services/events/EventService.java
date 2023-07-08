package com.actiongroup.actionserver.services.events;

import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.stats.Statistics;
import com.actiongroup.actionserver.models.users.Role;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.models.users.UserSettings;
import com.actiongroup.actionserver.repositories.events.EventRepository;
import com.actiongroup.actionserver.repositories.stats.StatisticsRepository;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final StatisticsRepository statisticsRepository;

    @Autowired
    public EventService(EventRepository eventRepository, StatisticsRepository statisticsRepository){
        this.eventRepository = eventRepository;
        this.statisticsRepository = statisticsRepository;
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

    public boolean save(Event event){
        // TODO разобараться с репозиторием и как искать (если существует -> return false)
//        User userFromDB = eventRepository.findBy....................();
//
//        if (userFromDB != null) {
//            return false;
//        }

        Statistics eventStat = new Statistics();
        eventStat.setAverage(0f);
        eventStat.setVotersCount(0);
        eventStat.setVisitorsCount(0);
        eventStat.setEvent(event);

        eventRepository.save(event);
        statisticsRepository.save(eventStat);
        return true;
    }

    public void deleteEvent(Event event){
        deleteStatistics(statisticsRepository.findByEvent(event));
        eventRepository.deleteById(event.getId());
    }

    public void deleteStatistics(Statistics statistics){
        statisticsRepository.deleteById(statistics.getId());
    }
}
