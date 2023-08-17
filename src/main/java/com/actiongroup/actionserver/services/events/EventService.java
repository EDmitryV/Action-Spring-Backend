package com.actiongroup.actionserver.services.events;

import com.actiongroup.actionserver.models.dto.TagDTO;
import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.events.Tag;
import com.actiongroup.actionserver.models.stats.Statistics;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.repositories.events.EventRepository;
import com.actiongroup.actionserver.repositories.events.TagRepository;
import com.actiongroup.actionserver.repositories.stats.StatisticsRepository;
import com.actiongroup.actionserver.services.users.RelationshipService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventService {
    private final RelationshipService relationshipService;
    private final EventRepository eventRepository;
    private final StatisticsRepository statisticsRepository;
    private final TagRepository tagRepository;

    private final EventPathService eventPathService;
    //TODO add get-recommended-list (not mvp)
    //TODO add get-hot-news (not mvp)

    public List<Event> getEventsOnMap(Point startPosition, Point endPosition, LocalDateTime startsAt, LocalDateTime endsAt, List<String> authors, List<TagDTO> tags, User user) {
        Set<Event> result = new HashSet<>();
        List<Event> events = eventRepository.findEventsOnMapWithTimeRange(startPosition.getX(), startPosition.getY(), endPosition.getX(), endPosition.getY(), startsAt, endsAt);
        for (int i = 0; i < events.size(); i++) {
            var hs1 = new HashSet(events.get(i).getTags());
            hs1.retainAll(tags);
            if (hs1.size() == 0) {
                events.remove(i);
                i--;
            }
        }
        if (authors.size() == 0) {
            result.addAll(events);
        } else {
            for (String author : authors) {
                switch (author) {
                    case "me" -> {
                        for (int i = 0; i < events.size(); i++) {
                            if (events.get(i).getOwner().getId() == user.getId()) {
                                result.add(events.get(i));
                                events.remove(i);
                                i--;
                            }
                        }
                    }
                    case "subscriptions" -> {
                        Set<User> subscriptions = relationshipService.getSubscriptions(user);
                        for (int i = 0; i < events.size(); i++) {
                            if (subscriptions.contains(events.get(i).getOwner())) {
                                result.add(events.get(i));
                                events.remove(i);
                                i--;
                            }
                        }
                    }
                    case "friends" -> {
                        Set<User> friends = relationshipService.getFriends(user);
                        for (int i = 0; i < events.size(); i++) {
                            if (friends.contains(events.get(i).getOwner())) {
                                result.add(events.get(i));
                                events.remove(i);
                                i--;
                            }
                        }
                    }
                }
            }
        }
        return new ArrayList<>(result);
    }

    public List<Event> getEventsOnMapNotAuth(Point startPosition, Point endPosition, LocalDateTime startsAt, LocalDateTime endsAt, List<TagDTO> tags) {
        List<Event> result = new ArrayList<>();
        List<Event> events = eventRepository.findEventsOnMapWithTimeRange(startPosition.getX(), startPosition.getY(), endPosition.getX(), endPosition.getY(), startsAt, endsAt);
        for (Event event : events) {
            var hs1 = new HashSet(event.getTags());
            hs1.retainAll(tags);
            if (hs1.size() != 0) {
                result.add(event);
            }
        }
        return new ArrayList<>(result);
    }

    public Event saveEvent(Event event) {
        if (event.getStatistics() == null) {
            Statistics eventStat = new Statistics();
            eventStat.setAverage(0f);
            eventStat.setVotersCount(0);
            eventStat.setVisitorsCount(0);
            event.setStatistics(eventStat);
        }
        return eventRepository.save(event);
    }


    public void deleteEventById(Long id) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null)
            return;
        deleteStatistics(statisticsRepository.findByEvent(event));
        eventPathService.deleteCheckpointsByEvent(event);
        eventRepository.deleteById(id);
    }

    public Event findEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public void deleteEvent(Event event) {
        deleteStatistics(statisticsRepository.findByEvent(event));
        eventPathService.deleteCheckpointsByEvent(event);
        eventRepository.deleteById(event.getId());
    }

    public void deleteStatistics(Statistics statistics) {
        statisticsRepository.deleteById(statistics.getId());
    }


    public Tag saveTag(Tag tag) {
        Tag foundTag = tagRepository.findByName(tag.getName());
        if (foundTag == null) {
            tagRepository.save(tag);
            foundTag = tagRepository.findByName(tag.getName());
        }

        return foundTag;
    }

    public void deleteTag(Tag tag) {
        List<Tag> children = getTagChildren(tag);
        if (children != null && children.size() != 0) {
            for (Tag ch : children)
                deleteTag(ch);
        }
        ;
        tagRepository.deleteById(tag.getId());
    }

    public List<Tag> getTagChildren(Tag parentTag) {
        return tagRepository.findByParentTag(parentTag);
    }

    public Tag findTagByName(String name) {
        return tagRepository.findByName(name);
    }

    public Tag findTagById(Long id) {
        return tagRepository.findById(id).orElse(null);
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }
}
