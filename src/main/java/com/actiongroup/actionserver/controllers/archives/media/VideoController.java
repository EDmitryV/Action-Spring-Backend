package com.actiongroup.actionserver.controllers.archives.media;

import com.actiongroup.actionserver.models.archives.VideoArchive;
import com.actiongroup.actionserver.models.archives.media.Image;
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
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user,
            @RequestParam("name") String name,
            @RequestParam("archive_id") Long id,
            @RequestParam("auto_repeat") Boolean autoRepeat
    ) throws IOException {
        Video video = new Video();
        video.setName(name != null ? name : video.getId().toString());
        video.setAutoRepeat(autoRepeat != null ? autoRepeat : false);
        video.setOwner(user);
        VideoArchive videoArchive;
        if (id != null && videoArchiveService.existsById(id)) {
            videoArchive = videoArchiveService.findById(id);
            if (!Objects.equals(videoArchive.getOwner().getId(), user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The user is not the owner of the archive.");
            }
        } else {
            videoArchive = new VideoArchive();
            videoArchive.setOwner(user);
            videoArchiveService.save(videoArchive);
        }
        video.setArchive(videoArchive);
        videoArchive.setContentCount(videoArchive.getContentCount() + 1);
        video = videoService.save(video);
        //TODO maybe remove this and make cascade?
//        videoArchiveService.save(videoArchive);
        videoService.saveWithFile(video, file);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteVideo(@PathVariable Long id, @AuthenticationPrincipal User user){
        Video video = videoService.findById(id);
        if(video == null)
            return ResponseEntity.badRequest().body("Video with id %s doesn't exists".formatted(id));
        if(!Objects.equals(video.getOwner().getId(), user.getId()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User isn't the owner of video");
        videoService.delete(video);
        return ResponseEntity.ok().body("Video is successfully deleted");
    }
}