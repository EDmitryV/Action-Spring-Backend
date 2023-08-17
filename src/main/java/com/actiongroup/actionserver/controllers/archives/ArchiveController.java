package com.actiongroup.actionserver.controllers.archives;

import com.actiongroup.actionserver.models.archives.*;
import com.actiongroup.actionserver.models.archives.media.Image;
import com.actiongroup.actionserver.models.archives.media.MediaType;
import com.actiongroup.actionserver.models.dto.*;

import com.actiongroup.actionserver.models.requests.CreateArchiveRequestBody;

import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.archives.EventsArchiveService;
import com.actiongroup.actionserver.services.archives.ImageArchiveService;
import com.actiongroup.actionserver.services.archives.AudioArchiveService;
import com.actiongroup.actionserver.services.archives.VideoArchiveService;
import com.actiongroup.actionserver.services.archives.media.ImageService;
import com.actiongroup.actionserver.services.users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping(value = "/archives")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ArchiveController {


    private final ImageArchiveService imageArchiveService;
    private final AudioArchiveService audioArchiveService;
    private final VideoArchiveService videoArchiveService;
    private final EventsArchiveService eventArchiveService;
    private final ImageService imageService;


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<ArchiveDTO> createArchive(@RequestBody CreateArchiveRequestBody body, @AuthenticationPrincipal User user) {
        if (body.getName() == null || body.getName().equals("")) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return switch (body.getType()) {
            case "audio" -> createAudioArchive(body, user);
            case "video" -> createVideoArchive(body, user);
            case "image" -> createImageArchive(body, user);
            case "event" -> createEventArchive(body, user);
            default -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        };
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create/audio")
    public ResponseEntity<ArchiveDTO> createAudioArchive(@RequestBody CreateArchiveRequestBody body, @AuthenticationPrincipal User user) {
        AudioArchive audioArchive = new AudioArchive();
        audioArchive.setName(body.getName());
        audioArchive.setOwner(user);
        return saveArchive(audioArchive, MediaType.Audio);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create/video")
    public ResponseEntity<ArchiveDTO> createVideoArchive(@RequestBody CreateArchiveRequestBody body, @AuthenticationPrincipal User user) {
        VideoArchive videoArchive = new VideoArchive();
        videoArchive.setName(body.getName());
        videoArchive.setOwner(user);
        return saveArchive(videoArchive, MediaType.Video);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create/event")
    public ResponseEntity<ArchiveDTO> createEventArchive(@RequestBody CreateArchiveRequestBody body, @AuthenticationPrincipal User user) {
        EventsArchive eventsArchive = new EventsArchive();
        eventsArchive.setName(body.getName());
        eventsArchive.setOwner(user);
        return saveArchive(eventsArchive, MediaType.Event);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create/image")
    public ResponseEntity<ArchiveDTO> createImageArchive(@RequestBody CreateArchiveRequestBody body, @AuthenticationPrincipal User user) {
        ImageArchive imageArchive = new ImageArchive();
        imageArchive.setName(body.getName());
        imageArchive.setOwner(user);
        return saveArchive(imageArchive, MediaType.Image);
    }

    private ResponseEntity<ArchiveDTO> saveArchive(Archive archive, MediaType mediaType) {
        switch (mediaType) {
            case Audio -> archive = audioArchiveService.save((AudioArchive) archive);
            case Image -> archive = imageArchiveService.save((ImageArchive) archive);
            case Event -> archive = eventArchiveService.save((EventsArchive) archive);
            case Video -> archive = videoArchiveService.save((VideoArchive) archive);
        }
        return new ResponseEntity<>(new ArchiveDTO(archive, false), HttpStatus.OK);
    }

    @GetMapping("/{archiveType}/get/{archiveId}")
    public ResponseEntity<ArchiveDTO> getArchiveById(@PathVariable("archiveType") String archiveType, @PathVariable("archiveId") Long archiveId) {
        switch (archiveType) {
            case "audio" -> {
                AudioArchive audioArchive = audioArchiveService.findById(archiveId);
                if (audioArchive == null)
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(new ArchiveDTO(audioArchive, true), HttpStatus.OK);
            }
            case "video" -> {
                VideoArchive videoArchive = videoArchiveService.findById(archiveId);
                if (videoArchive == null)
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(new ArchiveDTO(videoArchive, true), HttpStatus.OK);
            }
            case "image" -> {
                ImageArchive imageArchive = imageArchiveService.findById(archiveId);
                if (imageArchive == null)
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(new ArchiveDTO(imageArchive, true), HttpStatus.OK);
            }
            case "event" -> {
                EventsArchive eventsArchive = eventArchiveService.findById(archiveId);
                if (eventsArchive == null)
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(new ArchiveDTO(eventsArchive, true), HttpStatus.OK);
            }
            default -> {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update")
    public ResponseEntity<ArchiveDTO> updateArchive(@AuthenticationPrincipal User user, @RequestBody ArchiveDTO archive) {
        Archive oldArchive = switch (archive.getType()) {
            case "audio" -> audioArchiveService.findById(archive.getId());
            case "video" -> videoArchiveService.findById(archive.getId());
            case "image" -> imageArchiveService.findById(archive.getId());
            case "event" -> eventArchiveService.findById(archive.getId());
            default -> null;
        };
        if (oldArchive == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        if(!Objects.equals(oldArchive.getOwner().getId(), user.getId()))
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        oldArchive.setName(archive.getName());
        if (oldArchive.getCover().getId() != archive.getCoverId()) {
            Image image = imageService.findById(archive.getCoverId());
            if (image == null)
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            oldArchive.setCover(image);
        }
        return switch (archive.getType()) {
            case "audio" ->
                    new ResponseEntity<>(new ArchiveDTO(audioArchiveService.save((AudioArchive) oldArchive), false), HttpStatus.OK);
            case "video" ->
                    new ResponseEntity<>(new ArchiveDTO(videoArchiveService.save((VideoArchive) oldArchive), false), HttpStatus.OK);
            case "image" ->
                    new ResponseEntity<>(new ArchiveDTO(imageArchiveService.save((ImageArchive) oldArchive), false), HttpStatus.OK);
            case "event" ->
                    new ResponseEntity<>(new ArchiveDTO(eventArchiveService.save((EventsArchive) oldArchive), false), HttpStatus.OK);
            default -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        };
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{archiveType}/delete/{archiveId}")
    public ResponseEntity<String> deleteByArchiveTypeAndId(@PathVariable("archiveType") String archiveType, @PathVariable("archiveId") Long archiveId, @AuthenticationPrincipal User user) {
        Archive archive;
        switch (archiveType) {
            case "audio" -> archive = audioArchiveService.findById(archiveId);
            case "video" -> archive = videoArchiveService.findById(archiveId);
            case "image" -> archive = imageArchiveService.findById(archiveId);
            case "event" -> archive = eventArchiveService.findById(archiveId);
            default -> {
                return new ResponseEntity<>("Error: wrong type of archive", HttpStatus.BAD_REQUEST);
            }
        }
        if (archive == null)
            return new ResponseEntity<>("Error: archive with this id doesn't exists", HttpStatus.BAD_REQUEST);
        if (!Objects.equals(archive.getOwner().getId(), user.getId())) {
            return new ResponseEntity<>("Error: you are not the owner of archive", HttpStatus.FORBIDDEN);
        }
        switch (archiveType) {
            case "audio" -> audioArchiveService.delete((AudioArchive) archive);
            case "video" -> videoArchiveService.delete((VideoArchive) archive);
            case "image" -> imageArchiveService.delete((ImageArchive) archive);
            case "event" -> eventArchiveService.delete((EventsArchive) archive);
        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
