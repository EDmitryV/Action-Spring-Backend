package com.actiongroup.actionserver.controllers.chats;

import com.actiongroup.actionserver.models.chats.Chat;
import com.actiongroup.actionserver.models.chats.ChatNotification;
import com.actiongroup.actionserver.models.chats.Message;
import com.actiongroup.actionserver.models.dto.MessageDTO;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.chats.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat/{chat_id}")
    public MessageDTO processMessage(
            //@AuthenticationPrincipal User user,
            @DestinationVariable Long chat_id,
            @Payload MessageDTO messageDto){ //, @RequestParam(required = true) Long chatId){

            System.out.println(messageDto.getContent());
            System.out.println(chat_id);
            //message.setAuthor(user);
            Message msg = new Message();
            msg.setContent(messageDto.getContent());
            System.out.println("/topic/"+chat_id);
            messagingTemplate.convertAndSend("/topic/"+chat_id, msg);
            return messageDto;
    }

}
