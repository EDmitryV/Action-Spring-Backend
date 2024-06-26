package com.actiongroup.actionserver.repositories.users;

import com.actiongroup.actionserver.models.users.BlockerToBlockedRelation;
import com.actiongroup.actionserver.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlockerToBlockedRelationRepository extends JpaRepository<BlockerToBlockedRelation, Long> {
    BlockerToBlockedRelation findByBlockerAndBlocked(User blocker, User blocked);
    Boolean existsByBlockerAndBlocked(User blocker, User blocked);

    List<BlockerToBlockedRelation> findByBlocker(User blocker);
    List<BlockerToBlockedRelation> findByBlocked(User blocked);
}
