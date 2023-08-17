package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.chats.Chat;
import com.actiongroup.actionserver.models.users.User;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class ChatDTO {

    private Long id;
    private String name;
    private Long iconId;
    private Set<UserDTO> members;

    public ChatDTO(Chat chat){
        id = chat.getId();
        name = chat.getName();
        iconId = chat.getIcon().getId();
        members = new HashSet<>();
        for(User member :chat.getMembers()){
            members.add(new UserDTO(member, false));
        }
    }
}
