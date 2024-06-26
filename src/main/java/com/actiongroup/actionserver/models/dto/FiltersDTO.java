package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.events.Tag;
import lombok.Data;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class FiltersDTO {
    private  PointDTO startPoint;
    private  PointDTO endPoint;
    private  LocalDateTime startsAt;
    private  LocalDateTime endsAt;
    private  List<TagDTO> tags;
    //friends, subscriptions, me / all (if empty)
    private  List<String> authors;

    public FiltersDTO(){}

//    public FiltersDTO(Map<String, String> map, List<Tag>allTags){
//        startPoint = new PointDTO(map.get("startPoint"));
//        endPoint = new PointDTO(map.get("endPoint"));
//        startsAt = LocalDateTime.parse(map.get("startsAt"));
//        endsAt = LocalDateTime.parse(map.get("endsAt"));
//        authors =new ArrayList<>();
//        authors.addAll(List.of(map.get("authors").replace("[","").replace("]","").split(",")));
//        tags = new ArrayList<>();
//        tags.addAll(List.of(map.get("tags").replace("[","").replace("]","").split(",")));
//        if(tags.isEmpty()){
//            for(var)
//        }
//    }
}
