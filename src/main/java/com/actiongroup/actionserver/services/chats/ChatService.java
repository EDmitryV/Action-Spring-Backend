package com.actiongroup.actionserver.services.chats;

import com.actiongroup.actionserver.models.chats.Chat;
import com.actiongroup.actionserver.models.chats.Message;
import com.actiongroup.actionserver.repositories.chats.ChatRepository;
import com.actiongroup.actionserver.repositories.chats.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChatService {


    private ChatRepository chatRepository;
    private MessageRepository messageRepository;

    public Chat saveChat(Chat chat){
        return chatRepository.save(chat);
    }

    public Message saveMsg(Message msg){
        return messageRepository.save(msg);
    }

    public Chat findChatByid(Long id){
        return chatRepository.findById(id).orElse(null);
    }
}
