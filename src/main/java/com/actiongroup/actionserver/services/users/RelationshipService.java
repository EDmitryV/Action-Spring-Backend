package com.actiongroup.actionserver.services.users;

import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.models.users.UserRelation;
import com.actiongroup.actionserver.repositories.user.UserRelationRepository;
import com.actiongroup.actionserver.repositories.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RelationshipService {

    private UserRepository userRepository;
    private UserRelationRepository userRelationRepository;

    @Autowired
    public RelationshipService(UserRepository userRepository, UserRelationRepository userRelationRepository) {
        this.userRepository = userRepository;
        this.userRelationRepository = userRelationRepository;
    }

    public UserRelation subscribe(User source, User target){
        if(source.getId() == target.getId()) return null;

        UserRelation subscription = getRelationifExists(source,target,UserRelation.RelationTypes.Subscription);
        if(subscription == null)
            subscription = new UserRelation();
        subscription.setSourceUser(source);
        subscription.setTargetUser(target);
        subscription.setRelationType(UserRelation.RelationTypes.Subscription);
        userRelationRepository.save(subscription);
        return subscription;
    }

    public UserRelation block(User source, User target){
        if(source.getId() == target.getId()) return null;
        unsubscribe(source,target);

        UserRelation blockedRel = getRelationifExists(source,target,UserRelation.RelationTypes.Blocked);
        if(blockedRel == null)
            blockedRel = new UserRelation();
        blockedRel.setSourceUser(source);
        blockedRel.setTargetUser(target);
        blockedRel.setRelationType(UserRelation.RelationTypes.Blocked);
        userRelationRepository.save(blockedRel);
        return blockedRel;
    }

    public void unsubscribe(User source, User target){
        if(source.getId() == target.getId()) return;
        UserRelation subscription = getRelationifExists(source,target,UserRelation.RelationTypes.Subscription);
        if(subscription != null) delete(subscription);
    }

    public void removeFromBlackList(User source, User target){
        if(source.getId() == target.getId()) return;
        UserRelation block = getRelationifExists(source,target,UserRelation.RelationTypes.Blocked);
        if(block != null) delete(block);
    }

    public Set<User> getSubscriptions(User user){
        Set<User> result = new HashSet<>();
        List<UserRelation> subs = findBySourceUserAndRelationType(user, UserRelation.RelationTypes.Subscription);
        subs.forEach(userRelation -> result.add(userRelation.getTargetUser()));
        return result;
    }

    public Set<User> getFriends(User user){
        Set<User> result = new HashSet<>();
        Set<User> subscribtions = getSubscriptions(user);
        for(User target:subscribtions){
            UserRelation rel = getRelationifExists(target,user, UserRelation.RelationTypes.Subscription);
            if(rel != null)
                result.add(rel.getSourceUser());
        }
        return result;
    }



    public Set<User> getBlacklist(User user){
        Set<User> result = new HashSet<>();
        List<UserRelation> subs = findBySourceUserAndRelationType(user, UserRelation.RelationTypes.Blocked);
        subs.forEach(userRelation -> result.add(userRelation.getTargetUser()));
        return result;
    }


    public void delete(UserRelation rel){
        userRelationRepository.delete(rel);
    }

    private UserRelation getRelationifExists(User source, User target, UserRelation.RelationTypes type){
        List<UserRelation> rel = userRelationRepository
                .findBySourceUserAndTargetUserAndRelationType(source,target,type);
        if(rel != null && rel.size()>0) return rel.get(0);
        return null;
    }

    List<UserRelation> findBySourceUser(User source){
        return userRelationRepository.findBySourceUser(source);
    }

    List<UserRelation> findByTargetUser(User target){
        return userRelationRepository.findByTargetUser(target);
    };

    List<UserRelation> findBySourceUserAndRelationType(User source, UserRelation.RelationTypes type){
        return userRelationRepository.findBySourceUserAndRelationType(source, type);
    };
    List<UserRelation> findByTargetUserAndRelationType(User target, UserRelation.RelationTypes type){
        return userRelationRepository.findByTargetUserAndRelationType(target, type);
    };


}
