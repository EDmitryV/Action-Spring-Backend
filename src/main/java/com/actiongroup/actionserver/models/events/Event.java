package com.actiongroup.actionserver.models.events;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.models.chats.Chat;

@Entity
public class Event{
    
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
    

    @OneToOne()
    @JoinColumn(name="author_id", referencedColumnName = "id")
    private User author;


    @OneToOne()
    @JoinColumn(name="chat_id", referencedColumnName = "id")
    private Chat chat;
   
    
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }
}