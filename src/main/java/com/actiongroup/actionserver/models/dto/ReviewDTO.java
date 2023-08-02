package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.stats.Review;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDTO implements ApiDto{
    private Long eventId;
    private Long authorId;
    private Integer rating;
    private String content;
    private LocalDateTime publicationTime;


    public ReviewDTO(Review rev){
        eventId = rev.getEvent() == null ? -1: rev.getEvent().getId();
        authorId = rev.getAuthor() == null ? -1: rev.getAuthor().getId();
        rating = rev.getRating();
        content = rev.getContent();
        publicationTime = rev.getPublicationTime();
    }
}
