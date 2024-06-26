package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.archives.Archive;
import com.actiongroup.actionserver.models.archives.AudioArchive;
import com.actiongroup.actionserver.models.archives.media.Audio;
import com.actiongroup.actionserver.models.archives.media.Image;
import com.actiongroup.actionserver.models.archives.media.Media;
import com.actiongroup.actionserver.models.archives.media.Video;
import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.users.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
public class MediaDTO {
    private long id;
    private String name;
    private UserDTO owner;
    private long archiveId;
    //is null only for image
    private long coverId;
    private String type;
    //is null for everyone except video
    private boolean autoRepeat;
public MediaDTO(){}
    public MediaDTO(Media media) {
        Class<? extends Media> mediaClass = media.getClass();
        id = media.getId();
        name = media.getName();
        owner = new UserDTO(media.getOwner(), false);
        type = media.getType();
        if (mediaClass == Audio.class) {
            Audio audio = (Audio) media;
            archiveId = audio.getArchive().getId();
            if (audio.getCover() != null)
                coverId = audio.getCover().getId();
            type = "audio";
        } else if (mediaClass == Image.class) {
            Image image = (Image) media;
            archiveId = image.getArchive().getId();
            type = "image";
        } else if (mediaClass == Video.class) {
            Video video = (Video) media;
            archiveId = video.getArchive().getId();
            autoRepeat = video.getAutoRepeat();
            if (video.getCover() != null)
                coverId = video.getCover().getId();
            type = "video";
        } else if (mediaClass == Event.class) {
            Event event = (Event) media;
            archiveId = event.getArchive().getId();
            if (event.getCover() != null)
                coverId = event.getCover().getId();
            type = "event";
        }
    }
}
