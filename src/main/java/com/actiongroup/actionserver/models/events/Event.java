package com.actiongroup.actionserver.models.events;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.models.chats.Chat;
import lombok.Data;
import org.locationtech.jts.geom.Point;

@Data
@Entity
public class Event{
    
    public Event(){}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
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

}