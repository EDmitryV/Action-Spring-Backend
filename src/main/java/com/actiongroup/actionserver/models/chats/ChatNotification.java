package com.actiongroup.actionserver.models.chats;

import com.actiongroup.actionserver.models.dto.ApiDto;
import com.actiongroup.actionserver.models.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class ChatNotification {

//    @OneToOne
//    @JoinColumn(name = "sender_id", referencedColumnName = "id")
//    private User sender;

    private Long messageId;

    private ApiDto sender;

    private String content;

}
