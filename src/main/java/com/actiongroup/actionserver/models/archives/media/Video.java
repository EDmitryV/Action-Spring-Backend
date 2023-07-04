package com.actiongroup.actionserver.models.archives.media;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;

import com.actiongroup.actionserver.models.archives.VideoArchive;

@Entity
public class Video extends Media{
    public Video(){}

    @ManyToOne
    @JoinColumn(name = "archive_id", referencedColumnName = "id")
    private VideoArchive videoArchive;
    
    private Boolean autoRepeate;

    public VideoArchive getVideoArchive(){
        return videoArchive;
    }
    public void setVideoArchive(VideoArchive videoArchive){
        this.videoArchive = videoArchive;
    }

    public Boolean getAutoRepeate(){
        return autoRepeate;
    }
    public void setAutoRepeate(Boolean autoRepeate){
        this.autoRepeate = autoRepeate;
    }
}
