package com.actiongroup.actionserver.models.users;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Table(name = "blocker_to_blocked_relations")
@RequiredArgsConstructor
public class BlockerToBlockedRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected Long id;
    @ManyToOne
    private final User blocker;
    @ManyToOne
    private final User blocked;
}
