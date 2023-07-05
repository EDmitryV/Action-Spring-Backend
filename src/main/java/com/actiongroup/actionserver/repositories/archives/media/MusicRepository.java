package com.actiongroup.actionserver.repositories.archives.media;

import com.actiongroup.actionserver.models.archives.MusicArchive;
import com.actiongroup.actionserver.models.archives.media.Music;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicRepository extends JpaRepository<Music,Long> {
    public List<Music> findByMusicArchive(MusicArchive musicArchive);
}
