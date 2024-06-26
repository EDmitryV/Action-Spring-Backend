package com.actiongroup.actionserver.models.events;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Tag  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //TODO not required and needs in test
    @Nullable
    private Long parentId;

    private String name;

    private boolean isDeprecated;

    @Nullable
    @ManyToOne
    @JoinColumn(name="parent_tag_id", referencedColumnName = "id")
    private Tag parentTag;

    private String iconCode;

    public Tag(long id, String iconCode, boolean isDeprecated, String name){
        this.id = id;
        this.iconCode = iconCode;
        this.isDeprecated = isDeprecated;
        this.name = name;
    }
}
