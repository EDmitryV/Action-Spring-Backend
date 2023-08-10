package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.archives.Archive;
import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.models.users.UserSettings;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class UserFullDTO extends UserDTO {
    private String email;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private LocalDate birthDate;
    //TODO rewrite using DTO with ids instead of entities
    private UserSettings settings;
    private Set<ApiDto> friends;
    private Set<Archive> archives;
    private Set<Event> events;



    public UserFullDTO(User user){
        super(user);
        email = user.getEmail();
        firstname = user.getFirstname();
        lastname = user.getLastname();
        phoneNumber = user.getPhoneNumber();
        birthDate = user.getBirthDate();
        settings = user.getSettings();
        friends = new HashSet<>();
        //TODO Добавить все события
        events = new HashSet<>();
        //TODO Добавить все архивы
        archives = new HashSet<>();
    }

}

