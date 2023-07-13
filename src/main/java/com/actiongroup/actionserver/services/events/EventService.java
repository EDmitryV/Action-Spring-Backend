package com.actiongroup.actionserver.services.events;

import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.events.Tag;
import com.actiongroup.actionserver.models.stats.Statistics;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.repositories.events.EventRepository;
import com.actiongroup.actionserver.repositories.events.TagRepository;
import com.actiongroup.actionserver.repositories.stats.StatisticsRepository;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final StatisticsRepository statisticsRepository;
    private final TagRepository tagRepository;

    private final EventPathService eventPathService;

    @Autowired
    public EventService(EventRepository eventRepository, StatisticsRepository statisticsRepository, TagRepository tagRepository, EventPathService eventPathService){
        this.eventRepository = eventRepository;
        this.statisticsRepository = statisticsRepository;
        this.tagRepository = tagRepository;
        this.eventPathService = eventPathService;
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

    public Event saveEvent(Event event){
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

        Event ev = eventRepository.save(event);
        statisticsRepository.save(eventStat);
        return ev;
    }

    public Event findEventById(Long id){
        return eventRepository.findById(id).orElse(null);
    }

    public void deleteEvent(Event event){
        deleteStatistics(statisticsRepository.findByEvent(event));
        eventPathService.deleteCheckpointsByEvent(event);
        eventRepository.deleteById(event.getId());
    }

    public void deleteStatistics(Statistics statistics){
        statisticsRepository.deleteById(statistics.getId());
    }



    public Tag saveTag(Tag tag){
        Tag foundTag = tagRepository.findByName(tag.getName());
        if(foundTag == null) {
            tagRepository.save(tag);
            foundTag = tagRepository.findByName(tag.getName());
        }

        return foundTag;
    }

    public void deleteTag(Tag tag){
        List<Tag> children =  getTagChildren(tag);
        if(children!=null && children.size()!=0){
            for (Tag ch: children)
                deleteTag(ch);
        };
        tagRepository.deleteById(tag.getId());
    }

    public List<Tag> getTagChildren(Tag parentTag){
        return tagRepository.findByParentTag(parentTag);
    }

    public Tag findTagByName(String name){
        return tagRepository.findByName(name);
    }
    public Tag findTagById(Long id){
        return tagRepository.findById(id).orElse(null);
    }
}
