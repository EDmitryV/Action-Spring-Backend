package com.actiongroup.actionserver.repositories.archives.media;

import com.actiongroup.actionserver.models.archives.VideoArchive;
import com.actiongroup.actionserver.models.archives.media.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {

    public List<Video> findByVideoArchive(VideoArchive videoArchive);
}
