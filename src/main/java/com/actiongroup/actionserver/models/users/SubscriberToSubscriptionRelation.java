package com.actiongroup.actionserver.models.users;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Table(name = "subscriber_to_subscription_relations")
@RequiredArgsConstructor
public class SubscriberToSubscriptionRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    private final User subscriber;
    @ManyToOne
    private final User subscription;
}
