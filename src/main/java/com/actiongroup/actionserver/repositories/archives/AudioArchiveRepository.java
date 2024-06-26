package com.actiongroup.actionserver.repositories.archives;

import com.actiongroup.actionserver.models.archives.AudioArchive;
import com.actiongroup.actionserver.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface AudioArchiveRepository extends JpaRepository<AudioArchive,Long> {
    public Set<AudioArchive> findByOwner(User owner);
    public AudioArchive findByName(String name);
}
