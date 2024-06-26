package com.actiongroup.actionserver.repositories.archives.media;

import com.actiongroup.actionserver.models.archives.VideoArchive;
import com.actiongroup.actionserver.models.archives.media.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {

    public List<Video> findByArchive(VideoArchive archive);
    public Optional<Video> findByName(String name);
}
