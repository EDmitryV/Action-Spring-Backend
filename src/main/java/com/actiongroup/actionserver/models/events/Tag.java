package com.actiongroup.actionserver.models.events;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private boolean isDeprecated;

    @ManyToOne()
    @JoinColumn(name="parent_tag_id", referencedColumnName = "id")
    private Tag parentTag;

    private String iconNumber;

    private String iconTheme;
}
