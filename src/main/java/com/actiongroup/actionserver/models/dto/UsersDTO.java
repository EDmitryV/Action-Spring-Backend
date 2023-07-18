package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.users.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
@Getter
@Setter
@AllArgsConstructor
public class UsersDTO implements ApiDto{
    private List<UserSimpleDTO> users;
    public static UsersDTO toDTO(List<User> users){
        List<UserSimpleDTO> userDTOS = new ArrayList<>();
        for(User user : users) {
            userDTOS.add(new UserSimpleDTO(user)); // user==null учтено в конструкторе
        }
        return new UsersDTO(userDTOS);
    }
}
