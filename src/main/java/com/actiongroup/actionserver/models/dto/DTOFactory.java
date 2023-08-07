package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.archives.Archive;
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
                return new UserDTO(user);


            case Large:
                UserFullDTO dto =  new UserFullDTO(user);
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


    public ApiDto ArchiveToDto(Archive archive){
//        UserSimpleDTO userDto = (UserSimpleDTO) UserToDto(archive.getOwner(), UserDTOSettings.Simple);
        UserDTO userDto = new UserDTO(archive.getOwner());
        ArchiveDTO dto = new ArchiveDTO(archive);
        dto.setOwner(userDto);
        return dto;
    }

    public ApiDto ArchivesToDtoList(Set<? extends Archive> archives){
        Set<ApiDto> arDto = new HashSet<>();
        for(Archive ar: archives){
            arDto.add(ArchiveToDto(ar));
        }
        return new ApiDtoList(arDto);
    }
}
