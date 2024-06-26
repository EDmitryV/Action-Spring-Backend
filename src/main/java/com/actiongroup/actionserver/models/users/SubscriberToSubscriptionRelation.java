package com.actiongroup.actionserver.models.users;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@Table(name = "subscriber_to_subscription_relations")
@NoArgsConstructor
public class SubscriberToSubscriptionRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private  User subscriber;
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private  User subscription;
}
