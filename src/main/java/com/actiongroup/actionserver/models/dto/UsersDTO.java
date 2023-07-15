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
    private List<UserDTO> users;
    public static UsersDTO toDTO(List<User> users){
        List<UserDTO> userDTOS = new ArrayList<>();
        for(User user : users) {
            UserDTO dto = new UserDTO();
            if (user != null) {
                dto.setId(user.getId());
                dto.setUsername(user.getUsername());
                dto.setEmail(user.getEmail());
                dto.setFirstname(user.getFirstname());
                dto.setLastname(user.getLastname());
                dto.setPhoneNumber(user.getPhoneNumber());
                dto.setBirthDate(user.getBirthDate());
            }
            userDTOS.add(dto);
        }
        return new UsersDTO(userDTOS);
    }
}
