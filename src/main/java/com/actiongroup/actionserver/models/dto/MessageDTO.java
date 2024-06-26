package com.actiongroup.actionserver.models.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class MessageDTO {

    private String content;

    private LocalDateTime at;

    private Long chatId;

    private Long authorId;

public MessageDTO(){}
}
