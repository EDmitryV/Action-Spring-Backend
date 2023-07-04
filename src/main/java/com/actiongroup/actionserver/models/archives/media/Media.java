package com.actiongroup.actionserver.models.archives.media;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Media{
    
    @Id
    @GeneratedValue
    private Long id;
    
    protected String url;

    public Long getId(){
        return id;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }
}