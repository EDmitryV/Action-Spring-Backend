package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.archives.media.Video;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VideoDTO {
    private long id;
    private String name;
    private Boolean autoRepeat;
    private long ownerId;
    private long archiveId;
    public VideoDTO(Video video){
        id = video.getId();
        name = video.getName();
        autoRepeat = video.getAutoRepeat();
        ownerId = video.getOwner().getId();
        archiveId = video.getArchive().getId();
    }
}
