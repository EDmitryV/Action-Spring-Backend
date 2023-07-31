package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.archives.media.Image;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ImageDTO {
    private long id;
    private String name;
    private long ownerId;
    private long archiveId;

    public ImageDTO(Image image){
        id = image.getId();
        name = image.getName();
        ownerId = image.getOwner().getId();
        archiveId = image.getArchive().getId();
    }
}
