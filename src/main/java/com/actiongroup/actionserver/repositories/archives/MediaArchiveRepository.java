package com.actiongroup.actionserver.repositories.archives;

import com.actiongroup.actionserver.models.archives.Archive;
import com.actiongroup.actionserver.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaArchiveRepository<ArchiveType extends Archive>
        extends JpaRepository<ArchiveType, Long> {
    public ArchiveType findByOwner(User owner);

    public ArchiveType findByName(String name);
}
