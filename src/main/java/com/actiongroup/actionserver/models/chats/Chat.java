package com.actiongroup.actionserver.models.chats;

import com.actiongroup.actionserver.models.archives.media.Image;
import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.users.User;
import java.util.Set;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
public class Chat{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne()
    @JoinColumn(name="image_id", referencedColumnName = "id")
    private Image icon;

    @OneToOne()
    @JoinColumn(name="event_id", referencedColumnName = "id")
    private Event event;


    // TODO удаление записи при удалении пользователя
    @ManyToMany(cascade = CascadeType.DETACH)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="member_id", referencedColumnName = "id")
    private Set<User> members;
}