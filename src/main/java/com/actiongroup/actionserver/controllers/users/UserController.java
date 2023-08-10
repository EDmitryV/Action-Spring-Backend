package com.actiongroup.actionserver.controllers.users;

import com.actiongroup.actionserver.models.archives.ImageArchive;

import com.actiongroup.actionserver.models.dto.*;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.archives.AudioArchiveService;
import com.actiongroup.actionserver.services.archives.ImageArchiveService;
import com.actiongroup.actionserver.services.archives.VideoArchiveService;
import com.actiongroup.actionserver.services.users.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
@Tag(name = "Users API")
public class UserController {

    private UserService userService;
    private VideoArchiveService videoArchiveService;
    private AudioArchiveService audioArchiveService;
    private ImageArchiveService imageArchiveService;

    private DTOFactory dtoFactory;

    @Autowired
    public UserController(UserService userService, DTOFactory dtoFactory) {
        this.userService = userService;
        this.dtoFactory = dtoFactory;
    }


    @PreAuthorize("hasRole('USER')")
    @PutMapping("/edit")
    //TODO write descriptions on english
    @Operation(summary = "Edit authenticated user", description = "Обновляет все поля у пользоваетля")
    public ResponseEntity<ResponseWithDTO> editUser(@RequestBody UserFullDTO userdto, @AuthenticationPrincipal User user) {
        User userWithSameUsername = userService.findByUsername(userdto.getUsername());
        User userWithSameEmail = userService.findByEmail(userdto.getEmail());
        //Just for fun?.. (I don't understand why ObjectWithCopyableFields can't access to id of User anyway)
        if (userdto.getId() != user.getId()) {
            return new ResponseEntity<>(ResponseWithDTO.create(null, "Don't send id here"), HttpStatus.BAD_REQUEST);
        }
        if (userWithSameUsername != null && !userWithSameUsername.getId().equals(user.getId()))
            return new ResponseEntity<>(ResponseWithDTO.create(null, "User with this username already exists"), HttpStatus.BAD_REQUEST);
        if (userWithSameEmail != null && !userWithSameEmail.getId().equals(user.getId()))
            return new ResponseEntity<>(ResponseWithDTO.create(null, "User with this email already exists"), HttpStatus.BAD_REQUEST);
        //Some piece of cringe
        userdto.copyFieldsTo(user);
        userService.save(user);

        return new ResponseEntity<>(
                ResponseWithDTO.create(
                        dtoFactory.UserToDto(user, DTOFactory.UserDTOSettings.Simple),
                        "user successfully edited"), HttpStatus.OK);
    }


    @GetMapping("/get/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user successfully found"),
            @ApiResponse(responseCode = "400", description = "User was not found")
    })
    @Operation(summary = "Get user by id", description = "Возвращает короткое описание пользователя по его ID")
    public ResponseEntity<ResponseWithDTO> getUser(
            @PathVariable Long id) {
        return getUserResponse(userService.findById(id), DTOFactory.UserDTOSettings.Simple);
    }

    @GetMapping("/get-full/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user successfully found"),
            @ApiResponse(responseCode = "400", description = "User was not found")
    })
    @Operation(summary = "Get user by id", description = "Возвращает пользователя по его ID")
    public ResponseEntity<ResponseWithDTO> getUserFull(
            @PathVariable Long id) {
        return getUserResponse(userService.findById(id), DTOFactory.UserDTOSettings.Large);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me-full")
    public ResponseEntity<ResponseWithDTO> getAuthenticatedUserFull(
            @AuthenticationPrincipal User user) {
        return getUserResponse(user, DTOFactory.UserDTOSettings.Large);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me-full")
    public ResponseEntity<ResponseWithDTO> getAuthenticatedUser(
            @AuthenticationPrincipal User user) {
        return getUserResponse(user, DTOFactory.UserDTOSettings.Simple);
    }

    public ResponseEntity<ResponseWithDTO> getUserResponse(User user, DTOFactory.UserDTOSettings settings) {
        if (user == null)
            return new ResponseEntity<>(ResponseWithDTO.create(null, "user not found"), HttpStatus.BAD_REQUEST);


        return new ResponseEntity<>(
                ResponseWithDTO.create(
                        dtoFactory.UserToDto(user, settings),
                        "user successfully found"),
                HttpStatus.OK);
    }

    //TODO need in tests
    @GetMapping("/search")
    @Operation(summary = "Get list of users by part of username", description = "")
    public ResponseEntity<ResponseWithDTO> getUsers(@RequestParam("username") String username) {
        List<User> users = userService.findByUsernameContaining(username);
        return new ResponseEntity<>(ResponseWithDTO.create(UsersDTO.toDTO(users), "Users successfully found"), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete/me")
    @Operation(summary = "Delete authenticated user", description = "")
    public ResponseEntity<ResponseWithDTO> deleteUser(@AuthenticationPrincipal User user) {
        userService.deleteUser(user);
        return new ResponseEntity<>(ResponseWithDTO.create(null, "User successfully deleted"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete user by id", description = "Удаляет пользователя по его ID")
    public ResponseEntity<ResponseWithDTO> deleteUser(@PathVariable Long id) {
        User user = userService.findById(id);
        userService.deleteUser(user);
        return new ResponseEntity<>(ResponseWithDTO.create(null, "User successfully deleted"), HttpStatus.OK);
    }


}
