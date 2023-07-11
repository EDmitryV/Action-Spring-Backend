package com.actiongroup.actionserver.models.users;

import com.actiongroup.actionserver.models.EntityWithStatus;
import com.actiongroup.actionserver.models.chats.Chat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "users")
public class User extends EntityWithStatus {

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthDate;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private Set<Chat> chats;
}