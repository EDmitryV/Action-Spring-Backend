package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.ObjectWithCopyableFields;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.models.users.UserSettings;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Getter
@Setter
public class UserSimpleDTO extends ObjectWithCopyableFields implements ApiDto {

    private long id;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private LocalDate birthDate;
    private UserSettings settings;



    public static ApiDto create(User user){
        UserSimpleDTO dto = new UserSimpleDTO();
        if(user != null) {
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setFirstname(user.getFirstname());
            dto.setLastname(user.getLastname());
            dto.setPhoneNumber(user.getPhoneNumber());
            dto.setBirthDate(user.getBirthDate());
            dto.setSettings(user.getSettings());
        }
        return dto;
    }

    public UserSimpleDTO(){}

    public UserSimpleDTO(User user){
        if(user != null) {
            setId(user.getId());
            setUsername(user.getUsername());
            setEmail(user.getEmail());
            setFirstname(user.getFirstname());
            setLastname(user.getLastname());
            setPhoneNumber(user.getPhoneNumber());
            setBirthDate(user.getBirthDate());
            setSettings(user.getSettings());
        }
    }
}
