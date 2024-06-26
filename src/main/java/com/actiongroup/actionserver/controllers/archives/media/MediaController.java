package com.actiongroup.actionserver.controllers.archives.media;

import com.actiongroup.actionserver.controllers.events.EventController;
import com.actiongroup.actionserver.models.archives.AudioArchive;
import com.actiongroup.actionserver.models.archives.ImageArchive;
import com.actiongroup.actionserver.models.archives.VideoArchive;
import com.actiongroup.actionserver.models.archives.media.Audio;
import com.actiongroup.actionserver.models.archives.media.Image;
import com.actiongroup.actionserver.models.archives.media.Media;
import com.actiongroup.actionserver.models.archives.media.Video;
import com.actiongroup.actionserver.models.dto.MediaDTO;
import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.archives.AudioArchiveService;
import com.actiongroup.actionserver.services.archives.EventsArchiveService;
import com.actiongroup.actionserver.services.archives.ImageArchiveService;
import com.actiongroup.actionserver.services.archives.VideoArchiveService;
import com.actiongroup.actionserver.services.archives.media.*;
import com.actiongroup.actionserver.services.events.EventService;
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
import java.util.Optional;

@RestController
@RequestMapping("/media")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MediaController {
    private final ImageService imageService;
    private final ImageArchiveService imageArchiveService;
    private final MediaService mediaService;
    private final EventService eventService;
    private final EventsArchiveService eventsArchiveService;
    private final VideoService videoService;
    private final VideoArchiveService videoArchiveService;
    private final AudioService audioService;
    private final AudioArchiveService audioArchiveService;
    private final EventController eventController;

    @GetMapping(value = "audio/get/{id}", produces = "audio/mp3")
    public ResponseEntity<Resource> getAudio(@PathVariable Long id, @RequestHeader("Range") String range) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("audio/mp3"))
                .body(audioService.getAudioById(id));
    }

    @GetMapping(value = "/image/get/{id}", produces = "image/jpg")
    public ResponseEntity<Resource> getImage(@PathVariable Long id
    ) {
        if(id<=0)return ResponseEntity.badRequest().body(null);
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("image/jpg"))
                .body(imageService.getImageById(id));
    }

    @GetMapping(value = "/video/get/{id}", produces = "video/mp4")
    public ResponseEntity<Resource> getVideo(@PathVariable Long id, @RequestHeader("Range") String range) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("video/mp4"))
                .body(videoService.getVideoById(id));
    }

    @GetMapping("/{mediaType}/get-info/{id}")
    public ResponseEntity<MediaDTO> getMediaInfo(@PathVariable("mediaType") String mediaType, @PathVariable("id")Long id){
        switch (mediaType) {
            case "audio" -> {
                Audio audio = audioService.findById(id);
                if (audio == null)
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(new MediaDTO(audio), HttpStatus.OK);
            }
            case "video" -> {
                Video video = videoService.findById(id);
                if (video == null)
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(new MediaDTO(video), HttpStatus.OK);
            }
            case "image" -> {
                Image image = imageService.findById(id);
                if (image == null)
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(new MediaDTO(image), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{mediaType}/upload")
    public ResponseEntity<MediaDTO> upload(
            @PathVariable("mediaType") String mediaType,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user,
            @RequestParam("autoRepeat") Optional<Boolean> autoRepeat,
            @RequestParam("name") Optional<String> name,
            @RequestParam("archiveId") Optional<Long> archiveId,
            @RequestParam("imageId") Optional<Long> imageId) throws IOException {
        Media media;
        switch (mediaType) {
            case "audio" -> {
                media = new Audio();
                AudioArchive audioArchive;
                if (archiveId != null && audioArchiveService.existsById(archiveId.orElse(0L))) {
                    audioArchive = audioArchiveService.findById(archiveId.orElse(0L));
                    if (!Objects.equals(audioArchive.getOwner().getId(), user.getId())) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                    }
                } else {
                    audioArchive = new AudioArchive();
                    audioArchive.setOwner(user);
                    audioArchiveService.save(audioArchive);
                }
                ((Audio) media).setArchive(audioArchive);
                audioArchive.setContentCount(audioArchive.getContentCount() + 1);
            }
            case "video" -> {
                media = new Video();
                VideoArchive videoArchive;
                if (archiveId != null && videoArchiveService.existsById(archiveId.orElse(0L))) {
                    videoArchive = videoArchiveService.findById(archiveId.orElse(0L));
                    if (!Objects.equals(videoArchive.getOwner().getId(), user.getId())) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                    }
                } else {
                    videoArchive = new VideoArchive();
                    videoArchive.setOwner(user);
                    videoArchiveService.save(videoArchive);
                }
                ((Video) media).setArchive(videoArchive);
                videoArchive.setContentCount(videoArchive.getContentCount() + 1);
            }
            case "image" -> {
                media = new Image();
                ImageArchive imageArchive;
                if (archiveId != null && imageArchiveService.existsById(archiveId.orElse(0L))) {
                    imageArchive = imageArchiveService.findById(archiveId.orElse(0L));
                    if (!Objects.equals(imageArchive.getOwner().getId(), user.getId())) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                    }
                } else {
                    imageArchive = new ImageArchive();
                    imageArchive.setOwner(user);
                    imageArchiveService.save(imageArchive);
                }
                ((Image) media).setArchive(imageArchive);
                imageArchive.setContentCount(imageArchive.getContentCount() + 1);
            }
            default -> {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
        media.setName(name != null ? name.orElse("media") : media.getId().toString());
        media.setOwner(user);
        if (imageId != null && imageService.existsById(imageId.orElse(0L))) {
            Image image = imageService.findById(imageId.orElse(0L));
            if (!Objects.equals(image.getOwner().getId(), user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            media.setCover(image);
        }
        switch (mediaType) {
            case "audio" -> {
                Audio audio = audioService.save((Audio) media);
                mediaService.saveFile(file, MediaTypesPaths.AUDIO, user, audio.getArchive(), audio);
                return new ResponseEntity<>(new MediaDTO(audio), HttpStatus.OK);
            }
            case "video" -> {
                ((Video)media).setAutoRepeat(autoRepeat.orElse(false
                ));
                Video video = videoService.save((Video) media);
                mediaService.saveFile(file, MediaTypesPaths.VIDEO, user, video.getArchive(), video);
                return new ResponseEntity<>(new MediaDTO(video), HttpStatus.OK);
            }
            case "image" -> {
                Image image = imageService.save((Image) media);
                mediaService.saveFile(file, MediaTypesPaths.IMAGE, user, image.getArchive(), image);
                return new ResponseEntity<>(new MediaDTO(image), HttpStatus.OK);
            }
            default -> {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{mediaType}/delete/{id}")
    public ResponseEntity<String> deleteMedia(@PathVariable("mediaType") String mediaType, @PathVariable("id") Long id, @AuthenticationPrincipal User user) {
        Media media;
        switch (mediaType) {
            case "audio" -> media = audioService.findById(id);
            case "video" -> media = videoService.findById(id);
            case "image" -> media = imageService.findById(id);
            case "event" -> media = eventService.findEventById(id);
            default -> {
                return new ResponseEntity<>("Error: wrong media type", HttpStatus.BAD_REQUEST);
            }
        }
        if (media == null)
            return ResponseEntity.badRequest().body("Error: %s with id %s doesn't exists".formatted(mediaType, id));
        if (!Objects.equals(media.getOwner().getId(), user.getId()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: user isn't the owner of %s".formatted(mediaType));
        switch (mediaType) {
            case "audio" -> audioService.delete((Audio) media);
            case "image" -> imageService.delete((Image) media);
            case "video" -> videoService.delete((Video) media);
            case "event" -> eventService.deleteEvent((Event) media);
        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{mediaType}/update")
    public ResponseEntity<MediaDTO> updateMedia(@PathVariable("mediaType")String mediaType,
                                                @RequestBody MediaDTO mediaDTO,
                                                @AuthenticationPrincipal User user){
        Media media;
        switch (mediaType) {
            case "audio" -> media = audioService.findById(mediaDTO.getId());
            case "video" -> media = videoService.findById(mediaDTO.getId());
            case "image" -> media = imageService.findById(mediaDTO.getId());
            default -> {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
        if(media == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        if(!Objects.equals(media.getOwner().getId(), user.getId()))
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        media.setName(mediaDTO.getName());
        media.setCover(imageService.findById(mediaDTO.getCoverId()));
        switch (mediaType){
            case "audio" -> audioService.save((Audio)media);
            case "video" -> {
                ((Video)media).setAutoRepeat(mediaDTO.isAutoRepeat());
                videoService.save((Video) media);
            }
            case "image" -> imageService.save((Image)media);
        }
        return new ResponseEntity<>(new MediaDTO(media), HttpStatus.OK);
    }
}
