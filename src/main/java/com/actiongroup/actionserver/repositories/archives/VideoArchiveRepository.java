package com.actiongroup.actionserver.repositories.archives;

import com.actiongroup.actionserver.models.archives.MusicArchive;
import com.actiongroup.actionserver.models.archives.VideoArchive;
import com.actiongroup.actionserver.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoArchiveRepository extends JpaRepository<VideoArchive,Long> {
    public VideoArchive findByOwner(User owner);
    public VideoArchive findByName(String name);
}
