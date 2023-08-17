package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.users.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ContactsDTO {
    private final List<UserDTO> subscriptions;
    private final List<UserDTO> subscribers;
    private final List<UserDTO> friends;
}
