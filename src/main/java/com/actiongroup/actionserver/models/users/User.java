package com.actiongroup.actionserver.models.users;

import jakarta.persistence.*;
import java.util.Set;
import java.time.LocalDate;

@Entity
@Table(name = "t_user")
public class User{
    public User() {}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String surname;
    private String patronymic;
    private String phoneNumber;
    private LocalDate birthDate;

    
    private String username;
    private String password;
    @Transient
    private String passwordConfirm;



    @ManyToMany
    @JoinColumn(name = "friend_id", referencedColumnName = "id")
    private Set<User> Friends;
    
    @ManyToMany
    @JoinColumn(name = "subscription_id", referencedColumnName = "id")
    private Set<User> Subscriptions;

    @ManyToMany
    @JoinColumn(name = "blocked_user_id", referencedColumnName = "id")
    private Set<User> BlackList;

}