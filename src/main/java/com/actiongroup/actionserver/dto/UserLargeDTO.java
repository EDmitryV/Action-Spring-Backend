package com.actiongroup.actionserver.dto;

import com.actiongroup.actionserver.models.archives.Archive;
import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.models.users.UserRelation;
import com.actiongroup.actionserver.services.events.EventService;
import com.actiongroup.actionserver.services.users.RelationshipService;
import com.actiongroup.actionserver.services.users.UserService;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserLargeDTO  extends UserSimpleDTO{

    private Set<ApiDto> friends;
    private Set<Archive> archives;

    private Set<Event> events;



    public UserLargeDTO(User user){
        super(user);
        friends = new HashSet<>();
        //TODO Добавить все события
        events = new HashSet<>();
        //TODO Добавить все архивы
        archives = new HashSet<>();
    }

}

