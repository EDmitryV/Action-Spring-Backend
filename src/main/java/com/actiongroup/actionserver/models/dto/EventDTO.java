package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.archives.AudioArchive;
import com.actiongroup.actionserver.models.archives.EventsArchive;
import com.actiongroup.actionserver.models.archives.ImageArchive;
import com.actiongroup.actionserver.models.archives.VideoArchive;
import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.events.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EventDTO extends MediaDTO {
    private String description;

    private LocalDateTime startsAt;

    private LocalDateTime endsAt;

    private boolean isPrivate;

    private boolean isHotNews;
    private Point point;
    private ChatDTO chat;
    private List<ArchiveDTO> relatedAudioArchives;
    private List<ArchiveDTO> relatedImageArchives;
    private List<ArchiveDTO> relatedVideoArchives;
    private List<ArchiveDTO> relatedEventsArchive;
    private List<TagDTO> tags;
    private StatisticsDTO statistics;

    public EventDTO(Event event) {
        super(event);
        description = event.getDescription();
        startsAt = event.getStartsAt();
        endsAt = event.getEndsAt();
        isPrivate = event.isPrivate();
        chat = new ChatDTO(event.getChat());
        relatedEventsArchive = new ArrayList<>();
        for (EventsArchive eventsArchive : event.getRelatedEventsArchives()) {
            relatedEventsArchive.add(new ArchiveDTO(eventsArchive, true));
        }
        relatedAudioArchives = new ArrayList<>();
        for (AudioArchive audioArchive : event.getRelatedAudioArchives()) {
            relatedAudioArchives.add(new ArchiveDTO(audioArchive, true));
        }
        relatedImageArchives = new ArrayList<>();
        for (ImageArchive imageArchive : event.getRelatedImageArchives()) {
            relatedImageArchives.add(new ArchiveDTO(imageArchive, true));
        }
        relatedVideoArchives = new ArrayList<>();
        for (VideoArchive videoArchive : event.getRelatedVideoArchives()) {
            relatedVideoArchives.add(new ArchiveDTO(videoArchive, true));
        }
        tags = new ArrayList<>();
        for (Tag tag : event.getTags()) {
            tags.add(new TagDTO(tag));
        }
        statistics = new StatisticsDTO(event.getStatistics());
    }


}
