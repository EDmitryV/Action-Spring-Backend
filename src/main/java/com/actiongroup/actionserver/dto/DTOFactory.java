package com.actiongroup.actionserver.dto;

import com.actiongroup.actionserver.models.events.Tag;
import com.actiongroup.actionserver.models.users.User;

public class DTOFactory {

    public enum UserDTOSettings{
        Simple,
        Large
    }


    public static ApiDto UserToDto(User user, UserDTOSettings settings){
        switch (settings){
            case Simple:
                return new UserSimpleDTO(user);
            case Large:
                return new UserLargeDTO(user);
        }
        return null;
    }


    public static ApiDto TagToDto(Tag tag){
        if(tag == null) return new TagDTO();
        return new TagDTO(tag);
    }

}
