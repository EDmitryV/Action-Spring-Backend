package com.actiongroup.actionserver.repositories.archives;

import com.actiongroup.actionserver.models.archives.EventsArchive;
import com.actiongroup.actionserver.models.archives.ImageArchive;
import com.actiongroup.actionserver.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventArchiveRepository extends JpaRepository<EventsArchive, Long> {
    public EventsArchive findByOwner(User owner);
    public EventsArchive findByName(String name);
}
