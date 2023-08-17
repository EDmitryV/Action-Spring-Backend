package com.actiongroup.actionserver.models.events;

import com.actiongroup.actionserver.models.archives.EventsArchive;
import com.actiongroup.actionserver.models.archives.ImageArchive;
import com.actiongroup.actionserver.models.archives.AudioArchive;
import com.actiongroup.actionserver.models.archives.VideoArchive;
import com.actiongroup.actionserver.models.archives.media.Image;
import com.actiongroup.actionserver.models.archives.media.Media;
import com.actiongroup.actionserver.models.stats.Statistics;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.models.chats.Chat;
import lombok.Data;
import org.locationtech.jts.geom.Point;

@Data
@Entity
public class Event extends Media {

    public Event() {
    }

    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime startsAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime endsAt;

    private boolean isPrivate;

    private boolean isHotNews;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point point;

    @ManyToOne
    private Image cover;

    @ManyToOne()
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;


    @OneToOne()
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private Chat chat;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mainEvent")
    private Set<AudioArchive> relatedAudioArchives;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mainEvent")
    private Set<ImageArchive> relatedImageArchives;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mainEvent")
    private Set<VideoArchive> relatedVideoArchives;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mainEvent")
    private Set<EventsArchive> relatedEventsArchives;

    @ManyToOne()
    @JoinColumn(name = "archive_id", referencedColumnName = "id")
    private EventsArchive archive;

    @ManyToMany
    private Set<Tag> tags;

    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Statistics statistics;
}