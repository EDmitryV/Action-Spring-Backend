package com.actiongroup.actionserver.models.archives.media;
import com.actiongroup.actionserver.models.archives.AudioArchive;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Audio extends Media{
    @ManyToOne
    @JoinColumn(name = "archive_id", referencedColumnName = "id")
    private AudioArchive archive;

}