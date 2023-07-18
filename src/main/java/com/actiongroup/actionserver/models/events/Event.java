package com.actiongroup.actionserver.models.events;

import com.actiongroup.actionserver.models.EntityWithStatus;
import com.actiongroup.actionserver.models.archives.EventsArchive;
import com.actiongroup.actionserver.models.archives.ImageArchive;
import com.actiongroup.actionserver.models.archives.MusicArchive;
import com.actiongroup.actionserver.models.archives.VideoArchive;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.models.chats.Chat;
import lombok.Data;
import org.locationtech.jts.geom.Point;

@Data
@Entity
public class Event extends EntityWithStatus {
    
    public Event(){}

    
    private String type;
    
    private String name;

    private String description;

    private LocalDateTime startsAt;

    private LocalDateTime endsAt;
    
    private boolean isPrivate;

    private boolean isHot; // горячая новость

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point point;
    

    @OneToOne()
    @JoinColumn(name="author_id", referencedColumnName = "id")
    private User author;


    @OneToOne()
    @JoinColumn(name="chat_id", referencedColumnName = "id")
    private Chat chat;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="music_archvie_id", referencedColumnName = "id")
    private Set<MusicArchive> musicArchives;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="image_archvie_id", referencedColumnName = "id")
    private Set<ImageArchive> imageArchives;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="video_archvie_id", referencedColumnName = "id")
    private Set<VideoArchive> videoArchives;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="event_archvie_id", referencedColumnName = "id")
    private Set<EventsArchive> eventsArchives;
}