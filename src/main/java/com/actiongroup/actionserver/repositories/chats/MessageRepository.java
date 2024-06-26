package com.actiongroup.actionserver.repositories.chats;

import com.actiongroup.actionserver.models.chats.Chat;
import com.actiongroup.actionserver.models.chats.Message;
import com.actiongroup.actionserver.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChat(Chat chat);

    List<Message> findByAuthor(User user);

    List<Message> findByChatAndAuthor(Chat chat, User author);

    List<Message> findByAt(LocalDateTime at);

}
