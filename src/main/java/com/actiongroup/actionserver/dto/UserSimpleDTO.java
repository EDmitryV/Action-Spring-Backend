package com.actiongroup.actionserver.dto;

import com.actiongroup.actionserver.models.users.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Getter
@Setter
public class UserSimpleDTO implements ApiDto {

    private long id;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private LocalDate birthDate;



    public UserSimpleDTO(User user){
        if(user != null) {
            setId(user.getId());
            setUsername(user.getUsername());
            setEmail(user.getEmail());
            setFirstname(user.getFirstname());
            setLastname(user.getLastname());
            setPhoneNumber(user.getPhoneNumber());
            setBirthDate(user.getBirthDate());
        }
    }

    public UserSimpleDTO(){}

}
