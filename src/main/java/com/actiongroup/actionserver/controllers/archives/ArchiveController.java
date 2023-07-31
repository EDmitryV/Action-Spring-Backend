package com.actiongroup.actionserver.controllers.archives;

import com.actiongroup.actionserver.models.archives.*;
import com.actiongroup.actionserver.models.archives.media.Image;
import com.actiongroup.actionserver.models.dto.*;
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
    @PostMapping("/music/create")
    public ResponseEntity<ResponseWithDTO> createAudioArchive(@PathVariable("name") String name,
                                                      @AuthenticationPrincipal User user) {
        AudioArchive audioArchive = new AudioArchive();
        audioArchive.setName(name);
        audioArchive.setOwner(user);
        return saveArchive(audioArchive, Archive.Type.Audio);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/video/create")
    public ResponseEntity<ResponseWithDTO> createVideoArchive(@PathVariable("name") String name,
                                                      @AuthenticationPrincipal User user) {
        VideoArchive videoArchive = new VideoArchive();
        videoArchive.setName(name);
        videoArchive.setOwner(user);
        return saveArchive(videoArchive, Archive.Type.Video);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/event/create")
    public ResponseEntity<ResponseWithDTO> createEventArchive(@AuthenticationPrincipal User user,
                                                              @RequestParam("name") String name) {
        EventsArchive eventsArchive = new EventsArchive();
        eventsArchive.setName(name);
        eventsArchive.setOwner(user);
        return saveArchive(eventsArchive, Archive.Type.Event);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/image/create")
    public ResponseEntity<ResponseWithDTO> createImageArchive(@AuthenticationPrincipal User user,
                                                      @RequestParam("name") String name) {
        ImageArchive imageArchive = new ImageArchive();
        imageArchive.setName(name);
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


    @GetMapping("/music/get/{user_id}")
    public ResponseEntity<ResponseWithDTO> getArchiveMusic(@PathVariable("user_id") Long user_id) {
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
    public ResponseEntity<List<ArchiveDTO>> getAllMyArchives(@RequestParam Long user_id){
        User user = userService.findById(user_id);
        if (user == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        List<Archive> archives = new ArrayList<>();
        archives.addAll(user.getAudioArchives());
        archives.addAll(user.getVideoArchives());
        archives.addAll(user.getImageArchives());
        List<ArchiveDTO> archiveDTOs = new ArrayList<>();
        for(Archive archive:archives){
            archiveDTOs.add(new ArchiveDTO(archive));
        }
        return ResponseEntity.status(HttpStatus.OK).body(archiveDTOs);
    }
}
