package com.actiongroup.actionserver.repositories.chats;

import com.actiongroup.actionserver.models.chats.Chat;
import com.actiongroup.actionserver.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByName(String name);
    Set<Chat> findByMembers(User user);
}
