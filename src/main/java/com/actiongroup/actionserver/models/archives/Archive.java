package com.actiongroup.actionserver.models.archives;

import com.actiongroup.actionserver.models.users.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class Archive {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    private String name;

}