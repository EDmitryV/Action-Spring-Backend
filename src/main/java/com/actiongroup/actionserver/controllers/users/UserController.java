package com.actiongroup.actionserver.controllers.users;


import com.actiongroup.actionserver.models.archives.*;
import com.actiongroup.actionserver.models.dto.*;
import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.archives.media.ImageService;
import com.actiongroup.actionserver.services.events.EventService;
import com.actiongroup.actionserver.services.users.RelationshipService;
import com.actiongroup.actionserver.services.users.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
@Tag(name = "Users API")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserService userService;
    private final ImageService imageService;
    private final RelationshipService relationshipService;


    @PreAuthorize("hasRole('USER')")
    @PutMapping("/edit")
    //TODO write descriptions on english
    @Operation(summary = "Edit authenticated user", description = "Обновляет все поля у пользоваетля")
    public ResponseEntity<String> editUser(@RequestBody UserDTO userdto, @AuthenticationPrincipal User user) {
        User userWithSameUsername = userService.findByUsername(userdto.getUsername());
        User userWithSameEmail = userService.findByEmail(userdto.getEmail());
        if (userWithSameUsername != null && !userWithSameUsername.getId().equals(user.getId()))
            return new ResponseEntity<>("User with this username already exists", HttpStatus.BAD_REQUEST);
        if (userWithSameEmail != null && !userWithSameEmail.getId().equals(user.getId()))
            return new ResponseEntity<>("User with this email already exists", HttpStatus.BAD_REQUEST);

        if (userdto.getUsername() != null) user.setUsername(userdto.getUsername());
        if (userdto.getEmail() != null) user.setEmail(userdto.getEmail());
        if (userdto.getFirstname() != null) user.setFirstname(userdto.getFirstname());
        if (userdto.getLastname() != null) user.setLastname(userdto.getLastname());
        if (userdto.getPhoneNumber() != null) user.setPhoneNumber(userdto.getPhoneNumber());
        if (userdto.getBirthDate() != null) user.setBirthDate(userdto.getBirthDate());
        if (userdto.getIconId() != -1) user.setIconImage(imageService.findById(userdto.getIconId()));
        //TODO add edit for settings and password
        userService.save(user);

        return new ResponseEntity<>("Success:User edited successfully", HttpStatus.OK);
    }

    @GetMapping("/get-events/{id}")
    public ResponseEntity<List<EventDTO>> getAllEventsByUserId(@PathVariable("id") Long id) {
        List<EventDTO> response = new ArrayList<>();
        User user = userService.findById(id);
        if (user == null) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        for (EventsArchive eventsArchive : user.getEventsArchives()) {
            for (Event event : eventsArchive.getEvents()) {
                response.add(new EventDTO(event));
            }
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-archives/{id}")
    public ResponseEntity<List<ArchiveDTO>> getAllArchivesByUserId(@PathVariable("id") Long id) {
        List<ArchiveDTO> response = new ArrayList<>();
        User user = userService.findById(id);
        if (user == null) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        for (EventsArchive archive : user.getEventsArchives()) {
            response.add(new ArchiveDTO(archive, false));
        }
        for (ImageArchive archive : user.getImageArchives()) {
            response.add(new ArchiveDTO(archive, false));
        }
        for (AudioArchive archive : user.getAudioArchives()) {
            response.add(new ArchiveDTO(archive, false));
        }
        for (VideoArchive archive : user.getVideoArchives()) {
            response.add(new ArchiveDTO(archive, false));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-info/{id}")
    public ResponseEntity<UserDTO> getInfoByUserId(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        return new ResponseEntity<>(new UserDTO(user, true),HttpStatus.OK );
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-info/me")
    public ResponseEntity<UserDTO> getMyInfo(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(new UserDTO(user, true),HttpStatus.OK );
    }

    @GetMapping("/get-contacts/{id}")
    public ResponseEntity<ContactsDTO> getContactsByUserId(@PathVariable("id") Long id){
        User user = userService.findById(id);
        if(user == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        List<UserDTO> subscriptions = new ArrayList<>();
        for(User sub : relationshipService.getSubscriptions(user)){
            subscriptions.add(new UserDTO(sub, false));
        }
        List<UserDTO> subscribers = new ArrayList<>();
        for(User sub : relationshipService.getSubscribers(user)){
            subscribers.add(new UserDTO(sub, false));
        }
        List<UserDTO> friends = new ArrayList<>();
        for(User friend : relationshipService.getFriends(user)){
            friends.add(new UserDTO(friend, false));
        }
        List<UserDTO> blocked = new ArrayList<>();
        for(User block : relationshipService.getBlacklist(user)){
            blocked.add(new UserDTO(block, false));
        }
        return new ResponseEntity<>(new ContactsDTO(subscriptions, subscribers, friends, blocked), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-blocked-contacts")
    public ResponseEntity<List<UserDTO>> getBlockedContacts(@AuthenticationPrincipal User user) {
        List<UserDTO> blocked = new ArrayList<>();
        for (User blockedUser : relationshipService.getBlacklist(user)) {
            blocked.add(new UserDTO(blockedUser, false));
        }
        return new ResponseEntity<>(blocked, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/me")
    @Operation(summary = "Delete authenticated user", description = "")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal User user) {
        userService.deleteUser(user);
        return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete user by id", description = "Удаляет пользователя по его ID")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        User user = userService.findById(id);
        userService.deleteUser(user);
        return new ResponseEntity<>("User successfully deleted", HttpStatus.OK);
    }
}