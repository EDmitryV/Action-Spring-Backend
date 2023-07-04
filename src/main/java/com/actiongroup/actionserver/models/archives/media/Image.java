package com.actiongroup.actionserver.models.archives.media;

import com.actiongroup.actionserver.models.archives.ImageArchive;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;

@Entity
public class Image extends Media{
    
    public Image(){}

    @ManyToOne
    @JoinColumn(name = "archive_id", referencedColumnName = "id")
    private ImageArchive imageArchive;
    
    public ImageArchive getImageArchive(){
        return imageArchive;
    }

    public void setImageArchive(ImageArchive imageArchive){
        this.imageArchive = imageArchive;
    }
}
