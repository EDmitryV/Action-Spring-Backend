package com.actiongroup.actionserver.repositories.archives;

import com.actiongroup.actionserver.models.archives.ImageArchive;
import com.actiongroup.actionserver.models.archives.MusicArchive;
import com.actiongroup.actionserver.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageArchiveRepository extends JpaRepository<ImageArchive, Long> {
    public ImageArchive findByOwner(User owner);
    public ImageArchive findByName(String name);
}
