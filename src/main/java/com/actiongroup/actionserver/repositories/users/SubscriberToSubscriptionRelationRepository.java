package com.actiongroup.actionserver.repositories.users;

import com.actiongroup.actionserver.models.users.SubscriberToSubscriptionRelation;
import com.actiongroup.actionserver.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriberToSubscriptionRelationRepository extends JpaRepository<SubscriberToSubscriptionRelation, Long> {
    SubscriberToSubscriptionRelation findBySubscriberAndSubscription(User subscriber, User subscription);
    Boolean existsBySubscriberAndSubscription(User subscriber, User subscription);
    List<SubscriberToSubscriptionRelation> findBySubscriber(User subscriber);
    List<SubscriberToSubscriptionRelation> findBySubscription(User subscription);
}
