package com.actiongroup.actionserver.models.archives;

import com.actiongroup.actionserver.models.users.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;

@MappedSuperclass
public abstract class Archive {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne()
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    private String name;
    
    
    public Long getId() {
        return id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}