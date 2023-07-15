package com.actiongroup.actionserver.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Data
@NoArgsConstructor
public class EntityWithStatus extends ObjectWithCopyableFields {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected Long id;
    @Enumerated
    @Column(name = "status")
    protected Status status;
}
