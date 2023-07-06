package com.actiongroup.actionserver.models.archives;

import com.actiongroup.actionserver.models.users.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class Archive {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne()
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    private String name;

}