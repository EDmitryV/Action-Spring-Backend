package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.ObjectWithCopyableFields;
import com.actiongroup.actionserver.models.users.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Getter
@Setter
public class UserDTO extends ObjectWithCopyableFields implements ApiDto{

    private long id;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private LocalDate birthDate;



    public static UserDTO toDTO(User user){
        UserDTO dto = new UserDTO();
        if(user != null) {
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setFirstname(user.getFirstname());
            dto.setLastname(user.getLastname());
            dto.setPhoneNumber(user.getPhoneNumber());
            dto.setBirthDate(user.getBirthDate());
        }

        return dto;
    }



}
