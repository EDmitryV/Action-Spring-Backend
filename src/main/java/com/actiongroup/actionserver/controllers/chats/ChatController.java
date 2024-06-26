package com.actiongroup.actionserver.controllers.chats;

import com.actiongroup.actionserver.models.chats.Chat;
import com.actiongroup.actionserver.models.dto.ChatDTO;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.chats.ChatService;
import com.actiongroup.actionserver.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/chats")
public class ChatController {
    @Autowired
    private UserService userService;
    @Autowired
    private ChatService chatService;
    @PostMapping("/")
    public void createChat(@AuthenticationPrincipal User user,
                           @RequestBody Chat chat){
        Set<User> members = new HashSet<>();
        members.add(user);
        chat.setMembers(members);
        chatService.saveChat(chat);
    }


    @GetMapping("/")
    public ResponseEntity<List<ChatDTO>> getChats(@AuthenticationPrincipal User user){
        Set<Chat> chats = user.getChats();
        List<ChatDTO> response = new ArrayList<>();
        for(var chat:chats){
            response.add(new ChatDTO(chat));
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
