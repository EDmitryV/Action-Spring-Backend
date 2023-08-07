package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.ObjectWithCopyableFields;
import com.actiongroup.actionserver.models.users.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class UserDTO extends ObjectWithCopyableFields implements ApiDto {

    private long id;
    private String username;
    private long iconId;



    public static ApiDto create(User user){
        UserDTO dto = new UserDTO();
        if(user != null) {
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setIconId(user.getIconImage().getId());
        }
        return dto;
    }

    public UserDTO(){}

    public UserDTO(User user){
        if(user != null) {
            id = user.getId();
            username = user.getUsername();
            if(user.getIconImage() != null){
                iconId = user.getIconImage().getId();
            }
        }
    }
}
