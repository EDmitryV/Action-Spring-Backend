package com.actiongroup.actionserver.models.dto;

import lombok.Data;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class FiltersDTO {
    private final Point startPoint;
    private final Point endPoint;
    private final LocalDateTime startsAt;
    private final LocalDateTime endsAt;
    private final List<TagDTO> tags;
    //friends, subscriptions, me / all (if empty)
    private final List<String> authors;
}
