package com.actiongroup.actionserver.repositories.events;

import com.actiongroup.actionserver.models.events.EventPath;
import com.actiongroup.actionserver.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventPathRepository extends JpaRepository<EventPath,Long> {

    public List<EventPath> findByAuthor(User author);
}
