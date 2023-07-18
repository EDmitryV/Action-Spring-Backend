package com.actiongroup.actionserver.controllers.archives.media;

import com.actiongroup.actionserver.models.archives.VideoArchive;
import com.actiongroup.actionserver.models.archives.media.Video;
import com.actiongroup.actionserver.models.dto.VideoDTO;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.archives.VideoArchiveService;
import com.actiongroup.actionserver.services.archives.media.MediaStorageService;
import com.actiongroup.actionserver.services.archives.media.MediaTypesPaths;
import com.actiongroup.actionserver.services.archives.media.VideoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(value = "/video")
public class VideoStreamingController {
    private final MediaStorageService mediaStorageService;

    private final VideoService videoService;
    private final VideoArchiveService videoArchiveService;

    @GetMapping(value = "/get/{id}", produces = "video/mp4")
    public Mono<Resource> getVideo(@PathVariable Long id, @RequestHeader("Range") String range) {
        return videoService.getVideoById(id);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<Void> uploadFile(
            @RequestPart("file") Mono<FilePart> filePartMono,
            @RequestParam("filename") String name,
            @AuthenticationPrincipal User user,
            @RequestParam("archive_id") Long id,
            @RequestParam("auto_repeat") Boolean autoRepeat) {
        Video video = new Video();
        //TODO maybe problems (id may not be generated)
        video.setName(name != null ? name : video.getId().toString());
        video.setAutoRepeat(autoRepeat != null ? autoRepeat : false);
        video.setOwner(user);
        VideoArchive videoArchive;
        if (id != null && videoArchiveService.existsById(id)) {
            videoArchive = videoArchiveService.findById(id);
        } else {
            videoArchive = new VideoArchive();
            videoArchive.setOwner(user);
            //TODO maybe problems
            videoArchive.setName(videoArchive.getId().toString());
            videoArchiveService.save(videoArchive);
        }
        video.setVideoArchive(videoArchive);
        video = videoService.saveVideoEntity(video);
        return mediaStorageService.save(filePartMono, MediaTypesPaths.VIDEO, user, videoArchive, video);
    }
}