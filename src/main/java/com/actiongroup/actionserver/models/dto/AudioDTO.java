package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.archives.media.Audio;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AudioDTO {
    private long id;
    private String name;
    private long coverId;
    private long ownerId;
    private long archiveId;

    public AudioDTO(Audio audio){
        id = audio.getId();
        name = audio.getName();
        coverId =audio.getCover().getId();
        ownerId = audio.getOwner().getId();
        archiveId = audio.getArchive().getId();
    }
}
