package com.actiongroup.actionserver.services.archives;

import com.actiongroup.actionserver.models.archives.VideoArchive;
import com.actiongroup.actionserver.models.archives.media.Video;
import com.actiongroup.actionserver.repositories.archives.VideoArchiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoArchiveService {
    private final VideoArchiveRepository videoArchiveRepository;
    public VideoArchive findById(Long id){
        return videoArchiveRepository.findById(id).orElse(null);
    }
    public Boolean existsById(Long id){
        return videoArchiveRepository.existsById(id);
    }

    public VideoArchive save(VideoArchive videoArchive){
        return videoArchiveRepository.save(videoArchive);
    }
}
