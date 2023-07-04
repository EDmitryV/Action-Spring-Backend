package com.actiongroup.actionserver.models.archives.media;


import com.actiongroup.actionserver.models.archives.Archive;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;

@MappedSuperclass
public abstract class Media{
    
    @Id
    @GeneratedValue
    private Long id;

    
    private String url;

    // public abstract Archive getArchive();
}