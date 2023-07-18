package com.actiongroup.actionserver.repositories.archives;

import com.actiongroup.actionserver.models.archives.Archive;
import com.actiongroup.actionserver.models.archives.MusicArchive;
import com.actiongroup.actionserver.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface MusicArchiveRepository extends JpaRepository<MusicArchive,Long> {
    public Set<MusicArchive> findByOwner(User owner);
    public MusicArchive findByName(String name);
}
