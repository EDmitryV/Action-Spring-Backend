package com.actiongroup.actionserver.models.archives.media;
import com.actiongroup.actionserver.models.archives.MusicArchive;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Music extends Media{
    @ManyToOne
    @JoinColumn(name = "archive_id", referencedColumnName = "id")
    private MusicArchive musicArchive;

}