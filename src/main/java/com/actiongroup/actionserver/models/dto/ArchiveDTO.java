package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.archives.Archive;
import com.actiongroup.actionserver.models.users.User;
import lombok.Data;

@Data
public class ArchiveDTO implements ApiDto{

    private Long id;

    private UserSimpleDTO owner;

    private String name;

    public ArchiveDTO(Archive archive){
        id = archive.getId();
        name = archive.getName();
    }
}
