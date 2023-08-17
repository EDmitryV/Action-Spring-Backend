package com.actiongroup.actionserver.controllers.events;

import com.actiongroup.actionserver.models.archives.AudioArchive;
import com.actiongroup.actionserver.models.archives.EventsArchive;
import com.actiongroup.actionserver.models.archives.ImageArchive;
import com.actiongroup.actionserver.models.archives.VideoArchive;
import com.actiongroup.actionserver.models.archives.media.Audio;
import com.actiongroup.actionserver.models.archives.media.Image;
import com.actiongroup.actionserver.models.archives.media.Video;
import com.actiongroup.actionserver.models.chats.Chat;
import com.actiongroup.actionserver.models.dto.*;
import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.events.Tag;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.archives.AudioArchiveService;
import com.actiongroup.actionserver.services.archives.EventsArchiveService;
import com.actiongroup.actionserver.services.archives.ImageArchiveService;
import com.actiongroup.actionserver.services.archives.VideoArchiveService;
import com.actiongroup.actionserver.services.archives.media.AudioService;
import com.actiongroup.actionserver.services.archives.media.ImageService;
import com.actiongroup.actionserver.services.archives.media.VideoService;
import com.actiongroup.actionserver.services.chats.ChatService;
import com.actiongroup.actionserver.services.events.EventService;
import com.actiongroup.actionserver.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping(value = "/event")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventController {
    private final EventService eventService;
    private final EventsArchiveService eventsArchiveService;
    private final ImageArchiveService imageArchiveService;
    private final VideoArchiveService videoArchiveService;
    private final AudioArchiveService audioArchiveService;
    private final UserService userService;
    private final ChatService chatService;
    private final ImageService imageService;
    private final AudioService audioService;
    private final VideoService videoService;

    public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }
    //TODO add create
    //TODO add update
    //TODO add delete
    //TODO add get-recommended-list
    //TODO add get-hot-news

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-on-map")
    ResponseEntity<List<EventDTO>> getOnMap(@RequestBody FiltersDTO filters, @AuthenticationPrincipal User user) {
        List<Event> events = eventService.getEventsOnMap(filters.getStartPoint(), filters.getEndPoint(), filters.getStartsAt(), filters.getEndsAt(), filters.getAuthors(), filters.getTags(), user);
        List<EventDTO> result = new ArrayList<>();
        for (Event event : events) {
            result.add(new EventDTO(event));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/na/get-on-map")
    ResponseEntity<List<EventDTO>> getOnMapNotAuthorized(@RequestBody FiltersDTO filters) {
        List<Event> events = eventService.getEventsOnMapNotAuth(filters.getStartPoint(), filters.getEndPoint(), filters.getStartsAt(), filters.getEndsAt(), filters.getTags());
        List<EventDTO> result = new ArrayList<>();
        for (Event event : events) {
            result.add(new EventDTO(event));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create")
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO, @AuthenticationPrincipal User user) {
        Event event = new Event();
        if (eventDTO.getPoint() == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        else event.setPoint(eventDTO.getPoint());
        if (eventDTO.getChat() != null) {
            Chat chat = chatService.findById(eventDTO.getChat().getId());
            if (chat == null)
                chat = new Chat();
            chat.setName(eventDTO.getChat().getName() == null ? chat.getId().toString() : eventDTO.getChat().getName());
            chat.setIcon(eventDTO.getChat().getIconId() != null && imageService.existsById(eventDTO.getChat().getIconId()) ? imageService.findById(eventDTO.getChat().getIconId()) : null);
            Set<User> members = new HashSet<>();
            for(UserDTO member : eventDTO.getChat().getMembers()){
                User memberUser = userService.findById(member.getId());
                if(memberUser != null)
                    members.add(memberUser);
            }
            chat.setMembers(members);
            event.setChat(chat);
        }
        if (-1 != eventDTO.getCoverId()) {
            Image image = imageService.findById(eventDTO.getCoverId());
            if (image == null)
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            event.setCover(image);
        }
        event.setAuthor(user);
        if(eventDTO.getDescription() != null)
            event.setDescription(eventDTO.getDescription());
        long archiveId = eventDTO.getArchiveId();
        EventsArchive eventsArchive;
        if (archiveId == -1) {
            eventsArchive = new EventsArchive();
            eventsArchive.setName(eventsArchive.getId().toString());
            eventsArchive.setOwner(user);
            eventsArchiveService.save(eventsArchive);
        } else {
            eventsArchive = eventsArchiveService.findById(archiveId);
        }
        if (eventsArchive == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        event.setArchive(eventsArchive);
        //add related events archives
        List<ArchiveDTO> relatedEventsArchivesDTOs = eventDTO.getRelatedEventsArchive();
        Set<EventsArchive> relatedEventsArchives = new HashSet<>();
        for (ArchiveDTO archive : relatedEventsArchivesDTOs) {
            EventsArchive relatedEventsArchive = eventsArchiveService.findById(archive.getId());
            if (relatedEventsArchive == null) {
                relatedEventsArchive = new EventsArchive();
                if (archive.getName() != null)
                    relatedEventsArchive.setName(archive.getName());
                else relatedEventsArchive.setName(relatedEventsArchive.getId().toString());
                List<Event> relatedEvents = new ArrayList<>();
                for (MediaDTO relatedEventDTO : archive.getContent()) {
                    Event relatedEvent = eventService.findEventById(relatedEventDTO.getId());
                    if (relatedEvent != null)
                        relatedEvents.add(relatedEvent);
                }
                relatedEventsArchive.setEvents(relatedEvents);
            }
            relatedEventsArchives.add(relatedEventsArchive);
        }
        event.setRelatedEventsArchives(relatedEventsArchives);
        //add related image archives
        List<ArchiveDTO> relatedImageArchivesDTOs = eventDTO.getRelatedImageArchives();
        Set<ImageArchive> relatedImageArchives = new HashSet<>();
        for (ArchiveDTO archive : relatedImageArchivesDTOs) {
            ImageArchive relatedImageArchive = imageArchiveService.findById(archive.getId());
            if (relatedImageArchive == null) {
                relatedImageArchive = new ImageArchive();
                if (archive.getName() != null)
                    relatedImageArchive.setName(archive.getName());
                else relatedImageArchive.setName(relatedImageArchive.getId().toString());
                List<Image> relatedImages = new ArrayList<>();
                for (MediaDTO relatedImageDTO : archive.getContent()) {
                    Image relatedImage = imageService.findById(relatedImageDTO.getId());
                    if (relatedImage != null)
                        relatedImages.add(relatedImage);
                }
                relatedImageArchive.setImages(relatedImages);
            }
            relatedImageArchives.add(relatedImageArchive);
        }
        event.setRelatedImageArchives(relatedImageArchives);
        //add related audio archives
        List<ArchiveDTO> relatedAudioArchivesDTOs = eventDTO.getRelatedAudioArchives();
        Set<AudioArchive> relatedAudioArchives = new HashSet<>();
        for (ArchiveDTO archive : relatedAudioArchivesDTOs) {
            AudioArchive relatedAudioArchive = audioArchiveService.findById(archive.getId());
            if (relatedAudioArchive == null) {
                relatedAudioArchive = new AudioArchive();
                if (archive.getName() != null)
                    relatedAudioArchive.setName(archive.getName());
                else relatedAudioArchive.setName(relatedAudioArchive.getId().toString());
                List<Audio> relatedAudios = new ArrayList<>();
                for (MediaDTO relatedAudioDTO : archive.getContent()) {
                    Audio relatedAudio = audioService.findById(relatedAudioDTO.getId());
                    if (relatedAudio != null)
                        relatedAudios.add(relatedAudio);
                }
                relatedAudioArchive.setAudios(relatedAudios);
            }
            relatedAudioArchives.add(relatedAudioArchive);
        }
        event.setRelatedAudioArchives(relatedAudioArchives);
        //add related video archives
        List<ArchiveDTO> relatedVideoArchivesDTOs = eventDTO.getRelatedVideoArchives();
        Set<VideoArchive> relatedVideoArchives = new HashSet<>();
        for (ArchiveDTO archive : relatedVideoArchivesDTOs) {
            VideoArchive relatedVideoArchive = videoArchiveService.findById(archive.getId());
            if (relatedVideoArchive == null) {
                relatedVideoArchive = new VideoArchive();
                if (archive.getName() != null)
                    relatedVideoArchive.setName(archive.getName());
                else relatedVideoArchive.setName(relatedVideoArchive.getId().toString());
                List<Video> relatedVideos = new ArrayList<>();
                for (MediaDTO relatedVideoDTO : archive.getContent()) {
                    Video relatedVideo = videoService.findById(relatedVideoDTO.getId());
                    if (relatedVideo != null)
                        relatedVideos.add(relatedVideo);
                }
                relatedVideoArchive.setVideos(relatedVideos);
            }
            relatedVideoArchives.add(relatedVideoArchive);
        }
        event.setRelatedVideoArchives(relatedVideoArchives);
        //startsAt and endsAt
        if (eventDTO.getStartsAt() == null) {
            event.setStartsAt(LocalDateTime.now());
        } else {
            event.setStartsAt(eventDTO.getStartsAt());
        }
        if (eventDTO.getEndsAt() == null) {
            event.setEndsAt(LocalDateTime.now());
        } else {
            event.setEndsAt(eventDTO.getEndsAt());
        }
        //isPrivate and isHotNews
        event.setPrivate(eventDTO.isPrivate());
        event.setHotNews(eventDTO.isHotNews());
        //other
        Set<Tag> tags = new HashSet<>();
        for(TagDTO tagDTO : eventDTO.getTags()){
            Tag tag = eventService.findTagById(tagDTO.getId());
            if(tag != null)
                tags.add(tag);
        }
        if(tags.size() == 0)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        event.setTags(tags);
        return new ResponseEntity<>(new EventDTO(eventService.saveEvent(event)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/update")
    public ResponseEntity<EventDTO> updateEvent(@RequestBody EventDTO eventDTO, @AuthenticationPrincipal User user) {
        Event event = eventService.findEventById(eventDTO.getId());
        if(event == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        else if (!Objects.equals(event.getOwner().getId(), user.getId())) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        if (eventDTO.getPoint() != null)
            event.setPoint(eventDTO.getPoint());
        if (eventDTO.getChat() != null) {
            Chat chat = chatService.findById(eventDTO.getChat().getId());
            if (chat == null)
                chat = new Chat();
            chat.setName(eventDTO.getChat().getName() == null ? chat.getId().toString() : eventDTO.getChat().getName());
            chat.setIcon(eventDTO.getChat().getIconId() != null && imageService.existsById(eventDTO.getChat().getIconId()) ? imageService.findById(eventDTO.getChat().getIconId()) : null);
            Set<User> members = new HashSet<>();
            for(UserDTO member : eventDTO.getChat().getMembers()){
                User memberUser = userService.findById(member.getId());
                if(memberUser != null)
                    members.add(memberUser);
            }
            chat.setMembers(members);
            event.setChat(chat);
        }
        if (-1 != eventDTO.getCoverId()) {
            Image image = imageService.findById(eventDTO.getCoverId());
            if (image == null)
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            event.setCover(image);
        }
        if(eventDTO.getDescription() != null)
            event.setDescription(eventDTO.getDescription());
        if(eventDTO.getArchiveId() != -1 && eventDTO.getArchiveId() != event.getArchive().getId()) {
            EventsArchive archive = eventsArchiveService.findById(eventDTO.getArchiveId());
            if (archive!=null && Objects.equals(archive.getOwner().getId(), user.getId())){
                event.setArchive(archive);
            }
        }
        //add related events archives
        List<ArchiveDTO> relatedEventsArchivesDTOs = eventDTO.getRelatedEventsArchive();
        Set<EventsArchive> relatedEventsArchives = new HashSet<>();
        for (ArchiveDTO archive : relatedEventsArchivesDTOs) {
            EventsArchive relatedEventsArchive = eventsArchiveService.findById(archive.getId());
            if (relatedEventsArchive == null) {
                relatedEventsArchive = new EventsArchive();
                if (archive.getName() != null)
                    relatedEventsArchive.setName(archive.getName());
                else relatedEventsArchive.setName(relatedEventsArchive.getId().toString());
                List<Event> relatedEvents = new ArrayList<>();
                for (MediaDTO relatedEventDTO : archive.getContent()) {
                    Event relatedEvent = eventService.findEventById(relatedEventDTO.getId());
                    if (relatedEvent != null)
                        relatedEvents.add(relatedEvent);
                }
                relatedEventsArchive.setEvents(relatedEvents);
            }
            relatedEventsArchives.add(relatedEventsArchive);
        }
        event.setRelatedEventsArchives(relatedEventsArchives);
        //add related image archives
        List<ArchiveDTO> relatedImageArchivesDTOs = eventDTO.getRelatedImageArchives();
        Set<ImageArchive> relatedImageArchives = new HashSet<>();
        for (ArchiveDTO archive : relatedImageArchivesDTOs) {
            ImageArchive relatedImageArchive = imageArchiveService.findById(archive.getId());
            if (relatedImageArchive == null) {
                relatedImageArchive = new ImageArchive();
                if (archive.getName() != null)
                    relatedImageArchive.setName(archive.getName());
                else relatedImageArchive.setName(relatedImageArchive.getId().toString());
                List<Image> relatedImages = new ArrayList<>();
                for (MediaDTO relatedImageDTO : archive.getContent()) {
                    Image relatedImage = imageService.findById(relatedImageDTO.getId());
                    if (relatedImage != null)
                        relatedImages.add(relatedImage);
                }
                relatedImageArchive.setImages(relatedImages);
            }
            relatedImageArchives.add(relatedImageArchive);
        }
        event.setRelatedImageArchives(relatedImageArchives);
        //add related audio archives
        List<ArchiveDTO> relatedAudioArchivesDTOs = eventDTO.getRelatedAudioArchives();
        Set<AudioArchive> relatedAudioArchives = new HashSet<>();
        for (ArchiveDTO archive : relatedAudioArchivesDTOs) {
            AudioArchive relatedAudioArchive = audioArchiveService.findById(archive.getId());
            if (relatedAudioArchive == null) {
                relatedAudioArchive = new AudioArchive();
                if (archive.getName() != null)
                    relatedAudioArchive.setName(archive.getName());
                else relatedAudioArchive.setName(relatedAudioArchive.getId().toString());
                List<Audio> relatedAudios = new ArrayList<>();
                for (MediaDTO relatedAudioDTO : archive.getContent()) {
                    Audio relatedAudio = audioService.findById(relatedAudioDTO.getId());
                    if (relatedAudio != null)
                        relatedAudios.add(relatedAudio);
                }
                relatedAudioArchive.setAudios(relatedAudios);
            }
            relatedAudioArchives.add(relatedAudioArchive);
        }
        event.setRelatedAudioArchives(relatedAudioArchives);
        //add related video archives
        List<ArchiveDTO> relatedVideoArchivesDTOs = eventDTO.getRelatedVideoArchives();
        Set<VideoArchive> relatedVideoArchives = new HashSet<>();
        for (ArchiveDTO archive : relatedVideoArchivesDTOs) {
            VideoArchive relatedVideoArchive = videoArchiveService.findById(archive.getId());
            if (relatedVideoArchive == null) {
                relatedVideoArchive = new VideoArchive();
                if (archive.getName() != null)
                    relatedVideoArchive.setName(archive.getName());
                else relatedVideoArchive.setName(relatedVideoArchive.getId().toString());
                List<Video> relatedVideos = new ArrayList<>();
                for (MediaDTO relatedVideoDTO : archive.getContent()) {
                    Video relatedVideo = videoService.findById(relatedVideoDTO.getId());
                    if (relatedVideo != null)
                        relatedVideos.add(relatedVideo);
                }
                relatedVideoArchive.setVideos(relatedVideos);
            }
            relatedVideoArchives.add(relatedVideoArchive);
        }
        event.setRelatedVideoArchives(relatedVideoArchives);
        //startsAt and endsAt
        if (eventDTO.getStartsAt() != null) {
            event.setStartsAt(eventDTO.getStartsAt());
        }
        if (eventDTO.getEndsAt() != null) {
            event.setEndsAt(eventDTO.getEndsAt());
        }
        //isPrivate and isHotNews
        event.setPrivate(eventDTO.isPrivate());
        event.setHotNews(eventDTO.isHotNews());
        //other
        Set<Tag> tags = new HashSet<>();
        for(TagDTO tagDTO : eventDTO.getTags()){
            Tag tag = eventService.findTagById(tagDTO.getId());
            if(tag != null)
                tags.add(tag);
        }
        if(tags.size() == 0)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        event.setTags(tags);
        return new ResponseEntity<>(new EventDTO(eventService.saveEvent(event)), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable("id")Long id, @AuthenticationPrincipal User user){
        Event event = eventService.findEventById(id);
        if(event != null){
            if(Objects.equals(event.getOwner().getId(), user.getId())) {
                eventService.deleteEvent(event);
                return new ResponseEntity<>("Success", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Error: you are not the owner of event", HttpStatus.FORBIDDEN);
            }
        }else {
            return new ResponseEntity<>("Error: event with this id doesn't exists", HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/tags/get-all")
    public ResponseEntity<List<TagDTO>> getAllTags(){
        List<Tag> tagsList = eventService.getAllTags();
        List<TagDTO> tagsDTOs = new ArrayList<>();
        for(Tag tag :tagsList){
            tagsDTOs.add(new TagDTO(tag));
        }
        return new ResponseEntity<>(tagsDTOs, HttpStatus.OK);
    }

}