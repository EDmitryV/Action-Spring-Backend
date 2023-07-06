package com.actiongroup.actionserver.models.archives.media;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class Media{
    
    @Id
    @GeneratedValue
    private Long id;
    
    protected String url;
}