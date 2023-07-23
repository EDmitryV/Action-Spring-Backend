package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.chats.Message;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class MessageDTO implements ApiDto{

    private String content;

    private LocalDateTime at;

    private Long chatId;



}
