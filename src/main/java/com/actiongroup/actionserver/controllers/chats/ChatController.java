package com.actiongroup.actionserver.controllers.chats;

import com.actiongroup.actionserver.models.chats.Chat;
import com.actiongroup.actionserver.models.chats.ChatNotification;
import com.actiongroup.actionserver.models.chats.Message;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.chats.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @SendTo("topic/{chat_id}")
    public Message processMessage(
            //@AuthenticationPrincipal User user,
            @PathVariable Long chat_id,
            @Payload Message message){ //, @RequestParam(required = true) Long chatId){

            System.out.println(chat_id);
            System.out.println(message);
            //message.setAuthor(user);
            return message;


    }

}
