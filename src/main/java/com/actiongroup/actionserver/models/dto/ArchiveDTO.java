package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.archives.*;
import com.actiongroup.actionserver.models.archives.media.Audio;
import com.actiongroup.actionserver.models.archives.media.Image;
import com.actiongroup.actionserver.models.archives.media.MediaType;
import com.actiongroup.actionserver.models.archives.media.Video;
import com.actiongroup.actionserver.models.events.Event;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ArchiveDTO {


    private long id;
    private UserDTO owner;
    private String name;
    private long coverId;
    private String type;
    private long amount;
    private List<MediaDTO> content;

    public ArchiveDTO(Archive archive, boolean full) {
        id = archive.getId();
        name = archive.getName();
        owner = new UserDTO(archive.getOwner(), false);
        coverId = archive.getCover().getId();
        Class<? extends Archive> archiveClass = archive.getClass();
        amount = archive.getContentCount();
        if (archiveClass == AudioArchive.class) {
            type = MediaType.Audio.getMediaType();
        } else if (archiveClass == VideoArchive.class) {
            type = MediaType.Video.getMediaType();
        } else if (archiveClass == ImageArchive.class) {
            type = MediaType.Image.getMediaType();
        } else {
            type = MediaType.Event.getMediaType();
        }
        if (full) {
            content = new ArrayList<>();
            if (archiveClass == AudioArchive.class) {
                for (Audio audio : ((AudioArchive) archive).getAudios()) {
                    content.add(new MediaDTO(audio));
                }
            } else if (archiveClass == VideoArchive.class) {
                for (Video video : ((VideoArchive) archive).getVideos()) {
                    content.add(new MediaDTO(video));
                }
            } else if (archiveClass == ImageArchive.class) {
                for (Image image : ((ImageArchive) archive).getImages()) {
                    content.add(new MediaDTO(image));
                }
            } else {
                for (Event event : ((EventsArchive) archive).getEvents()) {
                    content.add(new MediaDTO(event));
                }
            }
        }
    }
}
