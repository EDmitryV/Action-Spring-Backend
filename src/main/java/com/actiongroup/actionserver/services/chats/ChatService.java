package com.actiongroup.actionserver.services.chats;

import com.actiongroup.actionserver.models.chats.Message;
import com.actiongroup.actionserver.models.chats.Chat;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.repositories.chats.ChatRepository;
import com.actiongroup.actionserver.repositories.chats.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChatService {


    @Autowired
    private ChatRepository chatRepository;
    @Autowired
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

    List<Message> findMessagesByChat(Chat chat){
        return messageRepository.findByChat(chat);
    }

    public Chat addMember(Chat chat,User user){
        Set<User> members = chat.getMembers();
        if(members == null) members = new HashSet<>();
        members.add(user);
        return saveChat(chat);
    }

    public Set<Chat> getChatsByUser(User member){
        return chatRepository.findByMembers(member);
    }

    public Chat findById(Long id) {
        return chatRepository.findById(id).orElse(null);
    }
}
