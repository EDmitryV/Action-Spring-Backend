package com.actiongroup.actionserver.controllers.archives;

import com.actiongroup.actionserver.models.archives.VideoArchive;
import com.actiongroup.actionserver.models.dto.VideoDTO;
import com.actiongroup.actionserver.services.archives.VideoArchiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(value = "/video-archive")
public class VideoArchiveController {
    private final VideoArchiveService videoArchiveService;

//    @GetMapping("/all-videos/get/{id}")
//    public ResponseEntity<List<VideoDTO>> getAllVideos(@PathVariable Long id){
//        VideoArchive videoArchive = videoArchiveService.findById(id);
//
//    }
}
