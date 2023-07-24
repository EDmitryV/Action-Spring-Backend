package com.actiongroup.actionserver.controllers.chats;

import com.actiongroup.actionserver.models.chats.Message;
import com.actiongroup.actionserver.models.dto.DTOFactory;
import com.actiongroup.actionserver.models.dto.MessageDTO;
import com.actiongroup.actionserver.services.chats.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWSController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @Autowired
    private DTOFactory dtoFactory;

    @MessageMapping("/chat/{chat_id}")
    public Message processMessage(
            @DestinationVariable Long chat_id,
            @Payload MessageDTO messageDto){
            Message msg = dtoFactory.DTOtoMsg(messageDto);
            messagingTemplate.convertAndSend("/topic/"+chat_id, msg);
            return msg;
    }

}
