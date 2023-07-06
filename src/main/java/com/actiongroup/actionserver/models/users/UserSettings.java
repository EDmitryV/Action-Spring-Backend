package com.actiongroup.actionserver.models.users;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class UserSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    private boolean isVerified;
}

