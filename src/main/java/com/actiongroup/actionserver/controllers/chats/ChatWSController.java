package com.actiongroup.actionserver.controllers.chats;

import com.actiongroup.actionserver.models.chats.Chat;
import com.actiongroup.actionserver.models.chats.ChatNotification;
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

import java.time.LocalDateTime;

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

            if(messageDto.getAt() == null) messageDto.setAt(LocalDateTime.now());
            Message msg = dtoFactory.DTOtoMsg(messageDto);

            Chat chat = chatService.findChatByid(chat_id);
            msg.setChat(chat);
            msg = chatService.saveMsg(msg);


            messagingTemplate.convertAndSend("/topic/"+chat_id, messageDto); //Рассылка сообщения в чат-комнату
            messagingTemplate.convertAndSend("/topic/"+chat_id+"/notifications", createNotification(msg)); // Рассылка уведомления о сообщении
            return msg;
    }

    private ChatNotification createNotification(Message msg){
        ChatNotification notification = new ChatNotification();
        notification.setMessageId(msg.getId());
        notification.setSender(dtoFactory.UserToDto(msg.getAuthor(), DTOFactory.UserDTOSettings.Simple));
        notification.setContent(msg.getContent());
        return notification;
    }

}
