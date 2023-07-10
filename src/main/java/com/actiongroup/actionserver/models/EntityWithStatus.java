package com.actiongroup.actionserver.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@MappedSuperclass
@Data
@NoArgsConstructor
public class EntityWithStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected Long id;
    @Enumerated
    @Column(name = "status")
    protected Status status;
}
