package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.archives.Archive;
import com.actiongroup.actionserver.models.chats.Chat;
import com.actiongroup.actionserver.models.chats.Message;
import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.events.Tag;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.chats.ChatService;
import com.actiongroup.actionserver.services.events.EventService;
import com.actiongroup.actionserver.services.users.RelationshipService;
import com.actiongroup.actionserver.services.users.UserService;
import org.geolatte.geom.M;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
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

    @Autowired
    private ChatService chatService;

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


    public ApiDto ArchiveToDto(Archive archive){
//        UserSimpleDTO userDto = (UserSimpleDTO) UserToDto(archive.getOwner(), UserDTOSettings.Simple);
        UserSmallDTO userDto = new UserSmallDTO(archive.getOwner());
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


    public Message DTOtoMsg(MessageDTO dto){
        Message msg = new Message();
        User author = userService.findById(dto.getAuthorId());
        msg.setAuthor(author);

        msg.setChat(chatService.findChatByid(dto.getChatId()));
        msg.setContent(dto.getContent());
        msg.setAt(dto.getAt());
        return msg;
    }

    public ApiDto ChatToDto(Chat chat){
        ChatDTO dto = new ChatDTO();
        dto.setId(chat.getId());
        dto.setName(chat.getName());
        HashSet<ApiDto> dtoMembers = new HashSet<>();
        for(User user: chat.getMembers()){
            ApiDto udto = this.UserToDto(user, UserDTOSettings.Simple);
            dtoMembers.add(udto);
        }
        dto.setMembers(dtoMembers);
        return dto;
    }

    public ApiDto ChatsToDtoList(Set<Chat> chats){
        Set<ApiDto> arDto = new HashSet<>();
        for(Chat c: chats){
            arDto.add(ChatToDto(c));
        }
        return new ApiDtoList(arDto);
    }


    public ApiDto toDTO(Event event) {
        EventDTO dto = new EventDTO();
        User author = event.getAuthor();
        if (author != null)
            dto.setAuthor(this.UserToDto(author, UserDTOSettings.Simple));
        dto.setHot(event.isHot());
        dto.setDescription(event.getDescription());
        dto.setType(event.getType());
        dto.setName(event.getName());
        dto.setPrivate(event.isPrivate());
        dto.setStartsAt(event.getStartsAt());
        dto.setEndsAt(event.getEndsAt());
        return dto;
    }
}
