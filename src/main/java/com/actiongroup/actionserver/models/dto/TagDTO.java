package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.events.Tag;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class TagDTO
        extends Tag
        implements ApiDto {

    public TagDTO(Tag tag){
        setId(tag.getId());
        setParentTag(tag.getParentTag());
        setName(tag.getName());
        setIcon(tag.getIcon());
        setDeprecated(tag.isDeprecated());

    }
}
