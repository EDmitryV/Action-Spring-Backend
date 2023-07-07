package com.actiongroup.actionserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDto {
    private String username;
    private String email;
    private String password;
}
