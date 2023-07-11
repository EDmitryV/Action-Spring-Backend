package com.actiongroup.actionserver.repositories.user;

import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.models.users.UserRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRelationRepository extends JpaRepository<UserRelation, Long> {

    List<UserRelation> findBySourceUser(User source);
    List<UserRelation> findByTargetUser(User target);
    List<UserRelation> findBySourceUserAndTargetUser(User source, User target);
    List<UserRelation> findBySourceUserOrTargetUser(User source, User target);

    List<UserRelation> findBySourceUserAndRelationType(User source, UserRelation.RelationTypes type);
    List<UserRelation> findByTargetUserAndRelationType(User target, UserRelation.RelationTypes type);
    List<UserRelation> findBySourceUserAndTargetUserAndRelationType(User source, User target, UserRelation.RelationTypes type);
}
