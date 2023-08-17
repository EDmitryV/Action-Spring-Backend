package com.actiongroup.actionserver.services.users;

import com.actiongroup.actionserver.models.users.SubscriberToSubscriptionRelation;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.models.users.BlockerToBlockedRelation;
import com.actiongroup.actionserver.repositories.users.BlockerToBlockedRelationRepository;
import com.actiongroup.actionserver.repositories.users.SubscriberToSubscriptionRelationRepository;
import com.actiongroup.actionserver.repositories.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RelationshipService {

    private final UserRepository userRepository;
    private final BlockerToBlockedRelationRepository blockerToBlockedRelationRepo;
    private final SubscriberToSubscriptionRelationRepository subscriberToSubscriptionRelationRepo;
    public SubscriberToSubscriptionRelation subscribe(User source, User target) {
        if (Objects.equals(source.getId(), target.getId())) return null;
        if (subscriberToSubscriptionRelationRepo.existsBySubscriberAndSubscription(source, target)) {
            return subscriberToSubscriptionRelationRepo.findBySubscriberAndSubscription(source, target);
        } else {
            SubscriberToSubscriptionRelation subscription = new SubscriberToSubscriptionRelation(source, target);
            subscriberToSubscriptionRelationRepo.save(subscription);
            return subscription;
        }
    }

    public BlockerToBlockedRelation block(User source, User target) {
        if (Objects.equals(source.getId(), target.getId())) return null;
        unsubscribe(source, target);

        if (blockerToBlockedRelationRepo.existsByBlockerAndBlocked(source, target)) {
            return blockerToBlockedRelationRepo.findByBlockerAndBlocked(source, target);
        } else {
            BlockerToBlockedRelation block = new BlockerToBlockedRelation(source, target);
            blockerToBlockedRelationRepo.save(block);
            return block;
        }
    }

    public void unsubscribe(User source, User target) {
        if (Objects.equals(source.getId(), target.getId())) return;
        if(subscriberToSubscriptionRelationRepo.existsBySubscriberAndSubscription(source, target)){
           SubscriberToSubscriptionRelation subscription = subscriberToSubscriptionRelationRepo.findBySubscriberAndSubscription(source, target);
           subscriberToSubscriptionRelationRepo.delete(subscription);
        }
    }

    public void removeFromBlackList(User source, User target) {
        if (Objects.equals(source.getId(), target.getId())) return;
        if(blockerToBlockedRelationRepo.existsByBlockerAndBlocked(source, target)){
            BlockerToBlockedRelation block = blockerToBlockedRelationRepo.findByBlockerAndBlocked(source, target);
            blockerToBlockedRelationRepo.delete(block);
        }
    }

    public Set<User> getSubscriptions(User user) {
        Set<User> result = new HashSet<>();
        List<SubscriberToSubscriptionRelation> subs = subscriberToSubscriptionRelationRepo.findBySubscriber(user);
        subs.forEach(blockerToBlockedRelation -> result.add(blockerToBlockedRelation.getSubscription()));
        return result;
    }

    public Set<User> getSubscribers(User user) {
        Set<User> result = new HashSet<>();
        List<SubscriberToSubscriptionRelation> subs = subscriberToSubscriptionRelationRepo.findBySubscription(user);
        subs.forEach(blockerToBlockedRelation -> result.add(blockerToBlockedRelation.getSubscriber()));
        return result;
    }

    public Set<User> getFriends(User user) {
        Set<User> result = new HashSet<>();
        Set<User> subscriptions = getSubscriptions(user);
        for (User subscription : subscriptions) {
            if(subscriberToSubscriptionRelationRepo.existsBySubscriberAndSubscription(subscription, user)){
                result.add(subscription);
            }
        }
        return result;
    }


    public Set<User> getBlacklist(User user) {
        Set<User> result = new HashSet<>();
        List<BlockerToBlockedRelation> blockedRelations = blockerToBlockedRelationRepo.findByBlocker(user);
        blockedRelations.forEach(blockerToBlockedRelation -> result.add(blockerToBlockedRelation.getBlocked()));
        return result;
    }


    public void deleteBlockerToBlockedRelation(BlockerToBlockedRelation rel) {
        blockerToBlockedRelationRepo.delete(rel);
    }

    public void deleteSubscriberToSubscriptionRelation(SubscriberToSubscriptionRelation rel) {
        subscriberToSubscriptionRelationRepo.delete(rel);
    }
}
