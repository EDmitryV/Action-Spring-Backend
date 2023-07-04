package com.actiongroup.actionserver.models.chats;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Chat{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

}