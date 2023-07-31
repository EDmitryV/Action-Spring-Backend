package com.actiongroup.actionserver.repositories.archives.media;

import com.actiongroup.actionserver.models.archives.AudioArchive;
import com.actiongroup.actionserver.models.archives.media.Audio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AudioRepository extends JpaRepository<Audio,Long> {
    public List<Audio> findByArchive(AudioArchive archive);
}
