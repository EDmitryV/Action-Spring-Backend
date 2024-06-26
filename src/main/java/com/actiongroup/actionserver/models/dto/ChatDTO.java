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
public ChatDTO(){}
    public ChatDTO(Chat chat){
    if(chat!=null) {
        id = chat.getId();
        name = chat.getName();
        iconId = chat.getIcon() != null ? chat.getIcon().getId() : -1;
        members = new HashSet<>();
        if (chat.getMembers() != null && !chat.getMembers().isEmpty())
            for (User member : chat.getMembers()) {
                members.add(new UserDTO(member, false));
            }
    }
    }
}
