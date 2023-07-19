package com.actiongroup.actionserver.controllers.chats;

import com.actiongroup.actionserver.models.chats.Chat;
import com.actiongroup.actionserver.models.chats.ChatNotification;
import com.actiongroup.actionserver.models.chats.Message;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.chats.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @MessageMapping("/chat")
    public void processMessage(@Payload Message message, @RequestParam(required = true) Long chatId){

        Chat chat = chatService.findChatByid(chatId);
        if(chat == null) return;

        message = chatService.saveMsg(message);



        for(User member: chat.getMembers()){
            messagingTemplate.convertAndSendToUser(
                member.getId().toString(), "/messages",
                new ChatNotification(message.getId(), member.getId(), member.getUsername())
            ); // Вроде как будет выглядеть типа /user/{member_id}/messages

        }

    }

}
