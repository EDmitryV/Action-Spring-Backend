package com.actiongroup.actionserver.controllers.archives;

import com.actiongroup.actionserver.models.archives.*;
import com.actiongroup.actionserver.models.archives.media.Image;
import com.actiongroup.actionserver.models.dto.*;
import com.actiongroup.actionserver.models.requests.CreateArchiveRequestBody;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.archives.EventsArchiveService;
import com.actiongroup.actionserver.services.archives.ImageArchiveService;
import com.actiongroup.actionserver.services.archives.AudioArchiveService;
import com.actiongroup.actionserver.services.archives.VideoArchiveService;
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
import java.util.Set;

@RestController
@RequestMapping(value = "/archives")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ArchiveController {


    private final ImageArchiveService imageArchiveService;
    private final AudioArchiveService audioArchiveService;
    private final VideoArchiveService videoArchiveService;
    private final EventsArchiveService eventArchiveService;
    private final UserService userService;
    private final DTOFactory dtoFactory;


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/audio/create")
    public ResponseEntity<ResponseWithDTO> createAudioArchive(@RequestBody CreateArchiveRequestBody body,
                                                              @AuthenticationPrincipal User user) {
        AudioArchive audioArchive = new AudioArchive();
        audioArchive.setName(body.getName());
        audioArchive.setOwner(user);
        return saveArchive(audioArchive, Archive.Type.Audio);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/video/create")
    public ResponseEntity<ResponseWithDTO> createVideoArchive(@RequestBody CreateArchiveRequestBody body,
                                                              @AuthenticationPrincipal User user) {
        VideoArchive videoArchive = new VideoArchive();
        videoArchive.setName(body.getName());
        videoArchive.setOwner(user);
        return saveArchive(videoArchive, Archive.Type.Video);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/event/create")
    public ResponseEntity<ResponseWithDTO> createEventArchive(@AuthenticationPrincipal User user,
                                                              @RequestBody CreateArchiveRequestBody body) {
        EventsArchive eventsArchive = new EventsArchive();
        eventsArchive.setName(body.getName());
        eventsArchive.setOwner(user);
        return saveArchive(eventsArchive, Archive.Type.Event);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/image/create")
    public ResponseEntity<ResponseWithDTO> createImageArchive(@AuthenticationPrincipal User user,
                                                              @RequestBody CreateArchiveRequestBody body
    ) {
        ImageArchive imageArchive = new ImageArchive();
        imageArchive.setName(body.getName());
        imageArchive.setOwner(user);
        return saveArchive(imageArchive, Archive.Type.Image);
    }

    private ResponseEntity<ResponseWithDTO> saveArchive(Archive archive, Archive.Type type) {
        switch (type) {
            case Audio:
                archive = audioArchiveService.save((AudioArchive) archive);
                break;
            case Image:
                archive = imageArchiveService.save((ImageArchive) archive);
                break;
            case Event:
                archive = eventArchiveService.save((EventsArchive) archive);
                break;
            case Video:
                archive = videoArchiveService.save((VideoArchive) archive);
                break;

            default:
                return new ResponseEntity<>(ResponseWithDTO.create(null, "smth went wrong"), HttpStatus.BAD_REQUEST);

        }
        ApiDto dto = dtoFactory.ArchiveToDto(archive);
        return new ResponseEntity<>(ResponseWithDTO.create(dto, "successful"), HttpStatus.OK);
    }


    @GetMapping("/audio/get/{user_id}")
    public ResponseEntity<ResponseWithDTO> getArchiveAudio(@PathVariable("user_id") Long user_id) {
        User user = userService.findById(user_id);
        if (user == null)
            return new ResponseEntity<>(ResponseWithDTO.create(null, "user with id " + user_id + " not found"), HttpStatus.BAD_REQUEST);

        Set<AudioArchive> archs = audioArchiveService.findByOwner(user);
        return new ResponseEntity<>(ResponseWithDTO.create(dtoFactory.ArchivesToDtoList(archs), "successfully"), HttpStatus.OK);
    }

    @GetMapping("/event/get/{user_id}")
    public ResponseEntity<ResponseWithDTO> getArchiveEvent(@PathVariable("user_id") Long user_id) {
        User user = userService.findById(user_id);
        if (user == null)
            return new ResponseEntity<>(ResponseWithDTO.create(null, "user with id " + user_id + " not found"), HttpStatus.BAD_REQUEST);
        Set<EventsArchive> archs = eventArchiveService.findByOwner(user);
        return new ResponseEntity<>(ResponseWithDTO.create(dtoFactory.ArchivesToDtoList(archs), "successfully"), HttpStatus.OK);
    }

    @GetMapping("/image/get/{user_id}")
    public ResponseEntity<ResponseWithDTO> getArchiveImage(@PathVariable("user_id") Long user_id) {
        User user = userService.findById(user_id);
        if (user == null)
            return new ResponseEntity<>(ResponseWithDTO.create(null, "user with id " + user_id + " not found"), HttpStatus.BAD_REQUEST);
        Set<ImageArchive> archs = imageArchiveService.findByOwner(user);
        return new ResponseEntity<>(ResponseWithDTO.create(dtoFactory.ArchivesToDtoList(archs), "successfully"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-all-archives/{user_id}")
    public ResponseEntity<List<ArchiveDTO>> getAllMyArchives(@PathVariable Long user_id) {
        User user = userService.findById(user_id);
        if (user == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        List<Archive> archives = new ArrayList<>();
        archives.addAll(user.getAudioArchives());
        archives.addAll(user.getVideoArchives());
        archives.addAll(user.getImageArchives());
        List<ArchiveDTO> archiveDTOs = new ArrayList<>();
        for (Archive archive : archives) {
            archiveDTOs.add(new ArchiveDTO(archive));
        }
        return ResponseEntity.status(HttpStatus.OK).body(archiveDTOs);
    }

    @GetMapping("/get-full/{archive_id}")
    public ResponseEntity<ArchiveDTO> getFullArchiveById(@PathVariable Long archive_id, @RequestParam("type") String type) {
        Archive archive;
        switch (type) {
            case "audio":
                archive = audioArchiveService.findById(archive_id);
                break;
            case "image":
                archive = imageArchiveService.findById(archive_id);
                break;
            case "video":
                archive = videoArchiveService.findById(archive_id);
                break;
            default:
                archive = eventArchiveService.findById(archive_id);
                break;
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ArchiveDTO(archive));
    }
}
