package com.actiongroup.actionserver.dto;

import com.actiongroup.actionserver.models.events.Tag;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.events.EventService;
import com.actiongroup.actionserver.services.users.RelationshipService;
import com.actiongroup.actionserver.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DTOFactory {
    @Autowired
    private UserService userService;
    @Autowired
    private RelationshipService relationshipService;
    @Autowired
    private EventService eventService;


    public enum UserDTOSettings{
        Simple,
        Large
    }


    public ApiDto UserToDto(User user, UserDTOSettings settings){
        switch (settings){
            case Simple:
                return new UserSimpleDTO(user);


            case Large:
                UserLargeDTO dto =  new UserLargeDTO(user);
                Set<User> friendsList= relationshipService.getFriends(user);
                if(friendsList==null) friendsList = new HashSet<>();
                for(User friend:friendsList){
                    dto.getFriends().add(UserToDto(friend, DTOFactory.UserDTOSettings.Simple) );
                }
                return dto;
        }
        return null;
    }


    public  ApiDto TagToDto(Tag tag){
        if(tag == null) return new TagDTO();
        return new TagDTO(tag);
    }

}
