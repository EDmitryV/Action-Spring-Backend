package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.users.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserSmallDTO {
    private long id;
    private String username;
    private long iconId;
    public UserSmallDTO(User user){
        id = user.getId();
        username = user.getUsername();
        iconId = user.getIconImage().getId();
    }
}
