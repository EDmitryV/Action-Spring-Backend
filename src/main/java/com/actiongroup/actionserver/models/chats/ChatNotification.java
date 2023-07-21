package com.actiongroup.actionserver.models.chats;

import com.actiongroup.actionserver.models.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ChatNotification {

//    @OneToOne
//    @JoinColumn(name = "sender_id", referencedColumnName = "id")
//    private User sender;

    private Long messageId;

    private Long senderId;
    private String senderName;

}
