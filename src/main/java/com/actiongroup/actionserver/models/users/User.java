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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}