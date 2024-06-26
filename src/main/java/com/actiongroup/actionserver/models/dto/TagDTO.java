package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.events.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Data
public class TagDTO{
    private Long id;
    private String name;
    private Long parentId;
    private boolean isDeprecated;
    private String iconCode;

public TagDTO(){}
    public TagDTO(Tag tag){
     id = tag.getId();
     name = tag.getName();
     parentId = tag.getParentId();
     isDeprecated = tag.isDeprecated();
     iconCode = tag.getIconCode();
    }
}
