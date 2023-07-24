package com.actiongroup.actionserver.models.dto;

import lombok.Data;

import java.util.Set;

@Data
public class ChatDTO implements ApiDto{

    private Long id;
    private String name;

    private Set<ApiDto> members;
}
