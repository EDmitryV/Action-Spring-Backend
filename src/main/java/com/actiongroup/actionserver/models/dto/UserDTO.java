package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.users.User;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;


@Data
public class UserDTO {

    private long id;
    private String username;
    private long iconId;
    //for full user
    private String email;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private LocalDate birthDate;
    private UserSettingsDTO settings;

    public UserDTO(User user, boolean full){
        if(user != null) {
            id = user.getId();
            username = user.getUsername();
            if(user.getIconImage() != null){
                iconId = user.getIconImage().getId();
            }else{
                iconId = -1;
            }
            if(full){
                email = user.getEmail();
                firstname = user.getFirstname();
                lastname = user.getLastname();
                phoneNumber = user.getPhoneNumber();
                birthDate = user.getBirthDate();
                settings = new UserSettingsDTO(user.getSettings());
            }
        }
    }
}
