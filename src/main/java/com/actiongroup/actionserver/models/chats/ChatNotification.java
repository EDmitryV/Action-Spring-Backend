package com.actiongroup.actionserver.models.chats;

import com.actiongroup.actionserver.models.dto.UserDTO;
import lombok.Data;


@Data
public class ChatNotification {

//    @OneToOne
//    @JoinColumn(name = "sender_id", referencedColumnName = "id")
//    private User sender;

    private Long messageId;

    private UserDTO sender;

    private String content;

}
