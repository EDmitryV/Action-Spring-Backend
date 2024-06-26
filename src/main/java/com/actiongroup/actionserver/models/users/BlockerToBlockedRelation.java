package com.actiongroup.actionserver.models.users;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Table(name = "blocker_to_blocked_relations")
@NoArgsConstructor
public class BlockerToBlockedRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User blocker;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User blocked;
}
