package com.actiongroup.actionserver.models.archives.media;

import com.actiongroup.actionserver.models.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class Media {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    protected String name;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    protected User owner;
    @ManyToOne
    protected Image cover;
    protected String type;
}