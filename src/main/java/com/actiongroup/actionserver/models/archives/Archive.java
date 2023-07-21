package com.actiongroup.actionserver.models.archives;

import com.actiongroup.actionserver.models.users.User;
import jakarta.persistence.*;
import lombok.Data;
import org.geolatte.geom.Circle;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Data
@MappedSuperclass
public abstract class Archive {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    private String name;

    public enum Type{
        Music,
        Video,
        Event,
        Image
    }

}