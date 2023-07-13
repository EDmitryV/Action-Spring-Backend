package com.actiongroup.actionserver.models.events;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class PathCheckpoint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="event_id", referencedColumnName = "id")
    private Event event;

    @ManyToOne
    @JoinColumn(name="event_path_id", referencedColumnName = "id")
    private EventPath parentPath;

    private Integer index;

}
