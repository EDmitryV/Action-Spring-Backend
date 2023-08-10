package com.actiongroup.actionserver.controllers.archives;

import com.actiongroup.actionserver.models.archives.VideoArchive;
import com.actiongroup.actionserver.models.archives.media.Image;
import com.actiongroup.actionserver.models.archives.media.Video;
import com.actiongroup.actionserver.models.dto.ArchiveDTO;
import com.actiongroup.actionserver.models.dto.ImageDTO;
import com.actiongroup.actionserver.models.dto.VideoDTO;
import com.actiongroup.actionserver.services.archives.ImageArchiveService;
import com.actiongroup.actionserver.services.archives.VideoArchiveService;
import com.actiongroup.actionserver.services.archives.media.ImageService;
import com.actiongroup.actionserver.services.archives.media.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(value = "/video-archive")
public class VideoArchiveController {
    private final VideoService videoService;
    private final VideoArchiveService videoArchiveService;


//    @GetMapping("/content/get/{id}")
//    public ResponseEntity<List<VideoDTO>> getArchiveContent(@PathVariable Long id, @RequestParam("on_page") int onPage, @RequestParam("page") int page) {
//        List<Video> videos = videoService.getVideosFromArchive(id, onPage, page);
//        List<VideoDTO> videoDTOs = new ArrayList<>();
//        for (Video video : videos) {
//            videoDTOs.add(new VideoDTO(video));
//        }
//        return ResponseEntity
//                .ok()
//                .body(videoDTOs);
//    }
//
//    @GetMapping("/info/get/{id}")
//    public ResponseEntity<ArchiveDTO> getArchiveMetaData(@PathVariable Long id) {
//        return ResponseEntity
//                .ok()
//                .body(new ArchiveDTO(videoArchiveService.findById(id)));
//    }

}
