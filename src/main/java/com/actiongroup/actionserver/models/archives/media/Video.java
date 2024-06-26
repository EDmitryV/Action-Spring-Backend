package com.actiongroup.actionserver.models.archives.media;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;

import com.actiongroup.actionserver.models.archives.VideoArchive;
import lombok.Data;

@Data
@Entity
public class Video extends Media{
    public Video(){}

    @ManyToOne
    @JoinColumn(name = "archive_id", referencedColumnName = "id")
    private VideoArchive archive;
    private Boolean autoRepeat;
}
