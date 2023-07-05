package com.actiongroup.actionserver.repositories.chats;

import com.actiongroup.actionserver.models.chats.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<String> findByName(String name);
}
