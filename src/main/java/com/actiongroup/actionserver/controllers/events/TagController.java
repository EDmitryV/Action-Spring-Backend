package com.actiongroup.actionserver.controllers.events;

import com.actiongroup.actionserver.dto.ResponseWithDTO;
import com.actiongroup.actionserver.dto.UserDTO;
import com.actiongroup.actionserver.models.events.Tag;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.events.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/events/tags")
public class TagController {

    private EventService eventService;

    @Autowired
    public TagController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "tag successfully found"),
            @ApiResponse(responseCode = "400", description = "tag was not found")
    })
    @Operation(summary = "Get tag by id", description = "Возвращает тег по его ID")
    public ResponseEntity<ResponseWithDTO<Tag>> getUser(@PathVariable Long id){
        Tag tag = eventService.findTagById(id);
        if(tag == null)
            return new ResponseEntity<>(ResponseWithDTO.create(new Tag(), "tag not found"), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(ResponseWithDTO.create(tag,"tag successfully found"),HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete tag by id", description = "Удаляет тег по его ID")
    public ResponseEntity<ResponseWithDTO> deleteUser(@PathVariable Long id){
        Tag tag = eventService.findTagById(id);
        eventService.deleteTag(tag);
        return new ResponseEntity<>(ResponseWithDTO.create(new Tag(), "tag deleted"),HttpStatus.OK);
    }


    @PostMapping("/")
    @Operation(summary = "Create tag", description = "Создание тега")
    public ResponseEntity<ResponseWithDTO> createUser(
            @RequestParam(name = "parent_id", required = false) Long parent_id,
            @RequestBody Tag tag){
        if(parent_id != null)
            tag.setParentTag(eventService.findTagById(parent_id));

        tag = eventService.saveTag(tag);
        return new ResponseEntity<>(ResponseWithDTO.create(tag, "tag created successfully"),HttpStatus.OK);
    }
}
