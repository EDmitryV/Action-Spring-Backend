package com.actiongroup.actionserver.models.users;




import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "t_user")
public class User{
    public User() {
    }

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
    
}