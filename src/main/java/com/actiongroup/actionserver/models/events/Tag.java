package com.actiongroup.actionserver.models.events;

import com.actiongroup.actionserver.models.dto.ApiDto;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Tag implements ApiDto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private boolean isDeprecated;

    @ManyToOne
    @JoinColumn(name="parent_tag_id", referencedColumnName = "id")
    private Tag parentTag;

    @Enumerated(EnumType.STRING)
    private Icon icon;


    enum Icon{
        None
    }
}
