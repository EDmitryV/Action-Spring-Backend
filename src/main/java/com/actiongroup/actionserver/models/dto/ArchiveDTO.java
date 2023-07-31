package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.archives.Archive;
import com.actiongroup.actionserver.models.archives.AudioArchive;
import com.actiongroup.actionserver.models.archives.VideoArchive;
import lombok.Data;

@Data
public class ArchiveDTO implements ApiDto {

    private Long id;

    private UserSmallDTO owner;

    private String name;

    private String type;

    public ArchiveDTO(Archive archive) {
        id = archive.getId();
        name = archive.getName();
        owner = new UserSmallDTO(archive.getOwner());
        Class<? extends Archive> archiveClass = archive.getClass();
        if (archiveClass == AudioArchive.class) {
            type = Archive.Type.Audio.getMediaType();
        } else if (archiveClass == VideoArchive.class) {
            type = Archive.Type.Video.getMediaType();
        } else {
            type = Archive.Type.Image.getMediaType();
        }
    }
}
