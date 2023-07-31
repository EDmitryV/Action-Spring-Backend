package com.actiongroup.actionserver.models.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventDTO implements ApiDto{

    private String type;

    private String name;

    private String description;

    private LocalDateTime startsAt;

    private LocalDateTime endsAt;

    private boolean isPrivate;

    private boolean isHot; // горячая новость

    private ApiDto author;


}
