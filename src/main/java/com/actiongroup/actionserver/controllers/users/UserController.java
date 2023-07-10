package com.actiongroup.actionserver.controllers.users;


import com.actiongroup.actionserver.dto.ResponseWithDTO;
import com.actiongroup.actionserver.dto.UserDTO;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.users.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "/users")
@Tag(name = "Users API")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/edit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user successfully edited"),
            @ApiResponse(responseCode = "400", description = "User was not found or cannot apply changes")
    })
    @Operation(summary = "Edit user by id", description = "Обновляет выбранные поля у пользоваетля")
    public ResponseEntity<ResponseWithDTO> editUser(@RequestBody UserDTO userdto){

        User user = userService.findById(userdto.getId());
        if(user == null)
            return new ResponseEntity<>(ResponseWithDTO.create(null, "user not found"), HttpStatus.BAD_REQUEST);
        if(userService.existsByUsername(userdto.getUsername()))
            return new ResponseEntity<>(ResponseWithDTO.create(null, "user already exists"), HttpStatus.BAD_REQUEST);

        if(userdto.getUsername() != null) user.setUsername(userdto.getUsername());
        if(userdto.getEmail() != null) user.setEmail(userdto.getEmail());
        if(userdto.getFirstName() != null) user.setFirstName(userdto.getFirstName());
        if(userdto.getLastName() != null) user.setLastName(userdto.getLastName());
        if(userdto.getBirthDate() != null) user.setBirthDate(userdto.getBirthDate());
        if(userdto.getPhoneNumber() != null) user.setPhoneNumber(userdto.getPhoneNumber());

        userService.save(user);

        return new ResponseEntity<>(
                ResponseWithDTO.create(UserDTO.toDTO(user), "user successfully edited"),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user successfully found"),
            @ApiResponse(responseCode = "400", description = "User was not found")
    })
    @Operation(summary = "Get user by id", description = "Возвращает пользователя по его ID")
    public ResponseEntity<ResponseWithDTO> getUser(@PathVariable Long id){
        User user = userService.findById(id);
        if(user == null)
            return new ResponseEntity<>(ResponseWithDTO.create(null, "user not found"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(ResponseWithDTO.create(UserDTO.toDTO(user), "user successfully found"),HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete user by id", description = "Удаляет пользователя по его ID")
    public ResponseEntity<ResponseWithDTO> deleteUser(@PathVariable Long id){
        User user = userService.findById(id);
        userService.deleteUser(user);
        return new ResponseEntity<>(ResponseWithDTO.create(null, "user deleted found"),HttpStatus.OK);
    }

}
