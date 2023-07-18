package com.actiongroup.actionserver.models.dto;

import lombok.Data;

import java.util.Set;

@Data
public class ApiDtoList implements ApiDto{

    private Set<ApiDto> objects;
    public ApiDtoList(Set<ApiDto> dtos){
        this.objects = dtos;
    }
}
