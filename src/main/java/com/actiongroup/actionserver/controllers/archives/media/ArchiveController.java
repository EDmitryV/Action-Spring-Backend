package com.actiongroup.actionserver.controllers.archives.media;

import com.actiongroup.actionserver.models.archives.*;
import com.actiongroup.actionserver.models.dto.*;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.archives.EventsArchiveService;
import com.actiongroup.actionserver.services.archives.ImageArchiveService;
import com.actiongroup.actionserver.services.archives.MusicArchiveService;
import com.actiongroup.actionserver.services.archives.VideoArchiveService;
import com.actiongroup.actionserver.services.events.EventService;
import com.actiongroup.actionserver.services.users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = "/archives")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ArchiveController {


    private final ImageArchiveService imageService;
    private final MusicArchiveService musicService;
    private final VideoArchiveService videoService;
    private final EventsArchiveService eventArchiveService;
    private final UserService userService;
    private final DTOFactory dtoFactory;


    @PostMapping("/{user_id}/music")
    public ResponseEntity<ResponseWithDTO> addArchive(@PathVariable("user_id") Long user_id,
                                                      @RequestBody MusicArchive musicArchive){
        User user = userService.findById(user_id);
        if(user == null) return new ResponseEntity<>(ResponseWithDTO.create(null, "user with id "+user_id+" not found"),HttpStatus.BAD_REQUEST);
        musicArchive.setOwner(user);
        return saveArchive(musicArchive, Archive.Type.Music);
    }
    @PostMapping("/{user_id}/video")
    public ResponseEntity<ResponseWithDTO> addArchive(@PathVariable("user_id") Long user_id,
                                                      @RequestBody VideoArchive videoArchive){
        User user = userService.findById(user_id);
        if(user == null) return new ResponseEntity<>(ResponseWithDTO.create(null, "user with id "+user_id+" not found"),HttpStatus.BAD_REQUEST);
        videoArchive.setOwner(user);
        return saveArchive(videoArchive, Archive.Type.Video);
    }

    @PostMapping("/{user_id}/event")
    public ResponseEntity<ResponseWithDTO> addArchive(@PathVariable("user_id") Long user_id,
                                                      @RequestBody EventsArchive eventsArchive){
        User user = userService.findById(user_id);
        if(user == null) return new ResponseEntity<>(ResponseWithDTO.create(null, "user with id "+user_id+" not found"),HttpStatus.BAD_REQUEST);
        eventsArchive.setOwner(user);
        return saveArchive(eventsArchive, Archive.Type.Event);
    }

    @PostMapping("/{user_id}/image")
    public ResponseEntity<ResponseWithDTO> addArchive(@PathVariable("user_id") Long user_id,
                                                      @RequestBody ImageArchive imageArchive){
        User user = userService.findById(user_id);
        if(user == null) return new ResponseEntity<>(ResponseWithDTO.create(null, "user with id "+user_id+" not found"),HttpStatus.BAD_REQUEST);
        imageArchive.setOwner(user);
        return saveArchive(imageArchive, Archive.Type.Image);
    }

    private ResponseEntity<ResponseWithDTO> saveArchive(Archive archive, Archive.Type type){
        switch (type){
            case Music:
                archive = musicService.save((MusicArchive) archive);
                break;
            case Image:
                archive = imageService.save((ImageArchive) archive);
                break;
            case Event:
                archive = eventArchiveService.save((EventsArchive) archive);
                break;
            case Video:
                archive = videoService.save((VideoArchive) archive);
                break;

            default:
                return new ResponseEntity<>(ResponseWithDTO.create(null, "smth went wrong"), HttpStatus.BAD_REQUEST);

        }
        ApiDto dto = dtoFactory.ArchiveToDto(archive);
        return new ResponseEntity<>(ResponseWithDTO.create(dto, "successful"), HttpStatus.OK);
    }



    @GetMapping("/{user_id}/music")
    public ResponseEntity<ResponseWithDTO> addArchiveMusic(@PathVariable("user_id") Long user_id){
        User user = userService.findById(user_id);
        if(user == null) return new ResponseEntity<>(ResponseWithDTO.create(null, "user with id "+user_id+" not found"),HttpStatus.BAD_REQUEST);

        Set<MusicArchive> archs =  musicService.findByOwner(user);
        return  new ResponseEntity<>(ResponseWithDTO.create(dtoFactory.ArchivesToDtoList(archs), "successfully"),HttpStatus.OK);
    }
//    @GetMapping("/{user_id}/video")
//    public ResponseEntity<ResponseWithDTO> addArchiveVideo(@PathVariable("user_id") Long user_id){
//        User user = userService.findById(user_id);
//        if(user == null) return new ResponseEntity<>(ResponseWithDTO.create(null, "user with id "+user_id+" not found"),HttpStatus.BAD_REQUEST);
//        Set<VideoArchive> archs =  videoService.findByOwner(user);
//        return  new ResponseEntity<>(ResponseWithDTO.create(dtoFactory.ArchivesToDtoList(archs), "successfully"),HttpStatus.OK);
//    }

    @GetMapping("/{user_id}/event")
    public ResponseEntity<ResponseWithDTO> addArchiveEvent(@PathVariable("user_id") Long user_id){
        User user = userService.findById(user_id);
        if(user == null) return new ResponseEntity<>(ResponseWithDTO.create(null, "user with id "+user_id+" not found"),HttpStatus.BAD_REQUEST);
        Set<EventsArchive> archs =  eventArchiveService.findByOwner(user);
        return  new ResponseEntity<>(ResponseWithDTO.create(dtoFactory.ArchivesToDtoList(archs), "successfully"),HttpStatus.OK);
    }

    @GetMapping("/{user_id}/image")
    public ResponseEntity<ResponseWithDTO> addArchiveimage(@PathVariable("user_id") Long user_id){
        User user = userService.findById(user_id);
        if(user == null) return new ResponseEntity<>(ResponseWithDTO.create(null, "user with id "+user_id+" not found"),HttpStatus.BAD_REQUEST);
        Set<ImageArchive> archs =  imageService.findByOwner(user);
        return  new ResponseEntity<>(ResponseWithDTO.create(dtoFactory.ArchivesToDtoList(archs), "successfully"),HttpStatus.OK);
    }
}
