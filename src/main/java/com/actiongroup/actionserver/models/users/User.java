package com.actiongroup.actionserver.models.users;

import com.actiongroup.actionserver.models.chats.Chat;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "users")
public class User {
    public User() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    private String firstName;
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;
    private String phoneNumber;
    private LocalDate birthDate;
    @Column(nullable = false)
    private String password;

    @ManyToMany
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Set<Role> roles;

    @ManyToMany
    @JoinColumn(name = "friend_id", referencedColumnName = "id")
    private Set<User> Friends;

    @ManyToMany
    @JoinColumn(name = "subscription_id", referencedColumnName = "id")
    private Set<User> Subscriptions;

    @ManyToMany
    @JoinColumn(name = "blocked_user_id", referencedColumnName = "id")
    private Set<User> BlackList;


    @ManyToMany
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private Set<Chat> chats;
}