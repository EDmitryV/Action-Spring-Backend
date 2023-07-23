package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.ObjectWithCopyableFields;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.awt.image.BufferedImage;

@Data
@AllArgsConstructor
public class VideoDTO {
    private long id;
    private String name;
    private Boolean autoRepeat;
    private BufferedImage firstFrame;
    private long fileSize;
    private long authorId;
    private long archiveId;
}
