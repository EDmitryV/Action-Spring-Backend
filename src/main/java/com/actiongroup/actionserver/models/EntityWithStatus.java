package com.actiongroup.actionserver.models;

import jakarta.persistence.*;
import lombok.Data;

@MappedSuperclass
@Data
public class EntityWithStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected final Long id;
    @Enumerated
    @Column(name = "status")
    protected Status status;
}
