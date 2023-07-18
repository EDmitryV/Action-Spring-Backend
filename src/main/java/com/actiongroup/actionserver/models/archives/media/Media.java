package com.actiongroup.actionserver.models.archives.media;

import com.actiongroup.actionserver.models.ObjectWithCopyableFields;
import com.actiongroup.actionserver.models.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class Media extends ObjectWithCopyableFields {

    @Id
    @GeneratedValue
    private Long id;
    //    @Column(unique = true)
    //    protected String url;
    @NotNull
    protected String name;
    @NotNull
    @ManyToOne
    protected User owner;
}