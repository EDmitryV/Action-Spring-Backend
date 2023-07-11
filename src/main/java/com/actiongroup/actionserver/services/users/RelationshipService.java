package com.actiongroup.actionserver.services.users;

import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.models.users.UserRelation;
import com.actiongroup.actionserver.repositories.user.UserRelationRepository;
import com.actiongroup.actionserver.repositories.user.UserRepository;
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

    public Set<User> getSubscriptions(User user){
        Set<User> result = new HashSet<>();
        List<UserRelation> subs = findBySourceUserAndRelationType(user, UserRelation.RelationTypes.Subscription);
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

//
//    public void sendFriendshipRequest(User source, User target){
//        UserRelation friendship = getRelationifExists(source,target,UserRelation.RelationTypes.FriendshipWaiting);
//        if(friendship == null)
//            friendship = new UserRelation();
//
//        friendship.setSourceUser(source);
//        friendship.setTargetUser(target);
//        friendship.setRelationType(UserRelation.RelationTypes.FriendshipWaiting);
//    }
//
//    public void acceptFriendship(User source, User target){
//        UserRelation friendsip = getRelationifExists(source,target,UserRelation.RelationTypes.FriendshipWaiting);
//        if(friendsip == null) return;
//        friendsip.setRelationType(UserRelation.RelationTypes.FriendshipAccepted);
//        userRelationRepository.save(friendsip);
//    }
//
//    public void declineFriendship(User source, User target){
//        UserRelation friendsip = getRelationifExists(source,target,UserRelation.RelationTypes.FriendshipWaiting);
//        if(friendsip == null) return;
//        friendsip.setRelationType(UserRelation.RelationTypes.FriendshipDeclined);
//        userRelationRepository.save(friendsip);
//    }
//
//
//    public void unfriend(User source, User target){
//        List<UserRelation> rel = userRelationRepository
//                .findBySourceUserAndTargetUserAndRelationType(source,target,UserRelation.RelationTypes.FriendshipWaiting);
//        if(rel == null) return;
//
//        UserRelation friendsip = rel.get(0);
//
//
//    }
//



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
