package com.actiongroup.actionserver.repositories.archives;

import com.actiongroup.actionserver.models.archives.EventsArchive;
import com.actiongroup.actionserver.models.archives.ImageArchive;
import com.actiongroup.actionserver.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface EventArchiveRepository extends JpaRepository<EventsArchive, Long> {
    public Set<EventsArchive> findByOwner(User owner);
    public EventsArchive findByName(String name);
}
