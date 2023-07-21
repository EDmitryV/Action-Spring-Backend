package com.actiongroup.actionserver.controllers.archives.media;

import com.actiongroup.actionserver.models.archives.VideoArchive;
import com.actiongroup.actionserver.models.archives.media.Video;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.archives.VideoArchiveService;
import com.actiongroup.actionserver.services.archives.media.MediaService;
import com.actiongroup.actionserver.services.archives.media.MediaTypesPaths;
import com.actiongroup.actionserver.services.archives.media.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(value = "/video")
public class VideoController {
    private final MediaService mediaService;

    private final VideoService videoService;
    private final VideoArchiveService videoArchiveService;

    @GetMapping(value = "/get/{id}", produces = "video/mp4")
    public ResponseEntity<Resource> getVideo(@PathVariable Long id, @RequestHeader("Range") String range) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("video/mp4"))
                .body(videoService.getVideoById(id));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/upload")
    public ResponseEntity<?> uploadVideo(
            @RequestParam("video") MultipartFile file,
            @AuthenticationPrincipal User user,
            @RequestParam("name") String name,
            @RequestParam("archive_id") Long id,
            @RequestParam("auto_repeat") Boolean autoRepeat) throws IOException {
        Video video = new Video();
        video.setName(name != null ? name : video.getId().toString());
        video.setAutoRepeat(autoRepeat != null ? autoRepeat : false);
        video.setOwner(user);
        VideoArchive videoArchive;
        if (id != null && videoArchiveService.existsById(id)) {
            videoArchive = videoArchiveService.findById(id);
            if(!Objects.equals(videoArchive.getOwner().getId(), user.getId())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The user is not the owner of the archive.");
            }
        } else {
            videoArchive = new VideoArchive();
            videoArchive.setOwner(user);
            videoArchiveService.save(videoArchive);
        }
        video.setVideoArchive(videoArchive);
        video = videoService.save(video);
        mediaService.saveFile(file, MediaTypesPaths.VIDEO, user, videoArchive, video);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}