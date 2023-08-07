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

@Data
@AllArgsConstructor
public class MediaDTO {
    private long id;
    private String name;
    private UserDTO owner;
    private long archiveId;
    private Boolean autoRepeat;
    private long coverId;

    public MediaDTO(Media media) {
        Class<? extends Media> mediaClass = media.getClass();
        id = media.getId();
        name = media.getName();
        owner = new UserDTO(media.getOwner());
        coverId = media.getCover().getId();
        if (mediaClass == Audio.class) {
            Audio audio = (Audio) media;
            archiveId = audio.getArchive().getId();
            coverId = audio.getCover().getId();
        } else if (mediaClass == Image.class) {
            Image image = (Image) media;
            archiveId = image.getArchive().getId();
        } else if (mediaClass == Video.class) {
            Video video = (Video) media;
            archiveId = video.getArchive().getId();
            autoRepeat = video.getAutoRepeat();
            coverId = video.getCover().getId();
        } else if (mediaClass == Event.class) {
            Event event = (Event) media;
            archiveId = event.getArchive().getId();
            coverId = event.getCover().getId();
        }
    }
}
