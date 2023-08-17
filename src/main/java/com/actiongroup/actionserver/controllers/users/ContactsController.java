package com.actiongroup.actionserver.controllers.users;

import com.actiongroup.actionserver.models.dto.UserDTO;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.users.RelationshipService;
import com.actiongroup.actionserver.services.users.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping(value = "/contacts")
@Tag(name = "Contacts API")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ContactsController {
    private final UserService userService;
    private final RelationshipService relationshipService;

    @GetMapping("/search")
    @Operation(summary = "Get list of users by part of username", description = "")
    public ResponseEntity<List<UserDTO>> getUsers(@RequestParam("username") String username) {
        List<User> users = userService.findByUsernameContaining(username);
        List<UserDTO> response = new ArrayList<>();
        for (User user : users) {
            response.add(new UserDTO(user, false));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/subscribe/{userId}")
    public ResponseEntity<String> subscribeByUserId(@PathVariable("userId") Long userId, @AuthenticationPrincipal User user) {
        User target = userService.findById(userId);
        if (target == null) {
            return new ResponseEntity<>("Error: user with this id doesn't exists", HttpStatus.BAD_REQUEST);
        }
        relationshipService.subscribe(user, target);
        List<UserDTO> response = new ArrayList<>();
        for(User sub : relationshipService.getSubscriptions(user)){
            response.add(new UserDTO(sub, false));
        }
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/unsubscribe/{userId}")
    public ResponseEntity<String> unsubscribeByUserId(@PathVariable("userId") Long userId, @AuthenticationPrincipal User user) {
        User target = userService.findById(userId);
        if (target == null ) {
            return new ResponseEntity<>("Error: user with this id doesn't exists", HttpStatus.BAD_REQUEST);
        }
        if(!(relationshipService.getSubscriptions(user).contains(target)||relationshipService.getFriends(user).contains(target))){
            return new ResponseEntity<>("Error: you are not subscribed on this user", HttpStatus.BAD_REQUEST);
        }
        relationshipService.unsubscribe(user, target);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/block/{userId}")
    public ResponseEntity<String> blockByUserId(@PathVariable("userId")Long userId, @AuthenticationPrincipal User user){
        User target = userService.findById(userId);
        if(target == null){
            return new ResponseEntity<>("Error: user with this id doesn't exists", HttpStatus.BAD_REQUEST);
        }
        relationshipService.block(user, target);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/unblock/{userId}")
    public ResponseEntity<String> unblockByUserId(@PathVariable("userId")Long userId, @AuthenticationPrincipal User user){
        User target = userService.findById(userId);
        if(target == null){
            return new ResponseEntity<>("Error: user with this id doesn't exists", HttpStatus.BAD_REQUEST);
        }
        if(!relationshipService.getBlacklist(user).contains(target)){
            return new ResponseEntity<>("Error: user with this id isn't blocked", HttpStatus.BAD_REQUEST);
        }
        relationshipService.removeFromBlackList(user, target);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

}
