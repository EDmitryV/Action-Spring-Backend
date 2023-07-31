package com.actiongroup.actionserver.controllers.archives.media;

import com.actiongroup.actionserver.models.archives.AudioArchive;
import com.actiongroup.actionserver.models.archives.media.Image;
import com.actiongroup.actionserver.models.archives.media.Audio;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.archives.AudioArchiveService;
import com.actiongroup.actionserver.services.archives.media.ImageService;
import com.actiongroup.actionserver.services.archives.media.MediaService;
import com.actiongroup.actionserver.services.archives.media.MediaTypesPaths;
import com.actiongroup.actionserver.services.archives.media.AudioService;
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
@RequestMapping(value = "/audio")
public class AudioController {
    private final MediaService mediaService;
    private final ImageService imageService;
    private final AudioService audioService;
    private final AudioArchiveService audioArchiveService;

    @GetMapping(value = "/get/{id}", produces = "audio/mp3")
    public ResponseEntity<Resource> getAudio(@PathVariable Long id, @RequestHeader("Range") String range) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("audio/mp3"))
                .body(audioService.getAudioById(id));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/upload")
    public ResponseEntity<?> uploadAudio(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user,
            @RequestParam("name") String name,
            @RequestParam("archive_id") Long archiveId,
            @RequestParam("image_id") Long imageId) throws IOException {
        Audio audio = new Audio();
        audio.setName(name != null ? name : audio.getId().toString());
        audio.setOwner(user);
        AudioArchive audioArchive;
        if (archiveId != null && audioArchiveService.existsById(archiveId)) {
            audioArchive = audioArchiveService.findById(archiveId);
            if (!Objects.equals(audioArchive.getOwner().getId(), user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The user is not the owner of the archive.");
            }
        } else {
            audioArchive = new AudioArchive();
            audioArchive.setOwner(user);
            audioArchiveService.save(audioArchive);
        }
        if (imageId != null && imageService.existsById(imageId)) {
            Image image = imageService.findById(imageId);
            if (!Objects.equals(image.getOwner().getId(), user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The user is not the owner of the image.");
            }
            audio.setCover(image);
        }
        audio.setArchive(audioArchive);
        audioArchive.setContentCount(audioArchive.getContentCount()+1);
        audio = audioService.save(audio);
        //TODO maybe remove this and make cascade?
//        audioArchiveService.save(audioArchive);
        mediaService.saveFile(file, MediaTypesPaths.MUSIC, user, audioArchive, audio);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteAudio(@PathVariable Long id, @AuthenticationPrincipal User user){
        Audio audio = audioService.findById(id);
        if(audio == null)
            return ResponseEntity.badRequest().body("Audio with id %s doesn't exists".formatted(id));
        if(!Objects.equals(audio.getOwner().getId(), user.getId()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User isn't the owner of audio");
        audioService.delete(audio);
        return ResponseEntity.ok().body("Audio is successfully deleted");
    }

}
