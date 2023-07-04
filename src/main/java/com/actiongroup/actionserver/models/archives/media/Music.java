package com.actiongroup.actionserver.models.archives.media;
import com.actiongroup.actionserver.models.archives.MusicArchive;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Music extends Media{


    public MusicArchive getMusicArchive(){
        return musicArchive;
    }
    public void setMusicArchive( MusicArchive musicArchive){
        this.musicArchive = musicArchive;
    }
    @ManyToOne
    @JoinColumn(name = "archive_id", referencedColumnName = "id")
    private MusicArchive musicArchive;



}