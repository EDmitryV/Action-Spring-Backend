package com.actiongroup.actionserver.controllers.stats;

import com.actiongroup.actionserver.models.dto.ResponseWithDTO;
import com.actiongroup.actionserver.models.dto.ReviewDTO;
import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.stats.Review;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.events.EventService;
import com.actiongroup.actionserver.services.stats.ReviewService;
import com.actiongroup.actionserver.services.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/statistics")
public class StatsController {

    private UserService userService;
    private ReviewService reviewService;
    private EventService eventService;

    @Autowired
    public StatsController(UserService userService, ReviewService reviewService, EventService eventService) {
        this.userService = userService;
        this.reviewService = reviewService;
        this.eventService = eventService;
    }



    @PostMapping("/reviews/add")
    public ResponseEntity<ResponseWithDTO> createReview(@RequestBody ReviewDTO reviewDTO){ //TODO @AuthenticationPrincipal ??
        User author = userService.findById(reviewDTO.getAuthorId());
        Event event = eventService.findEventById(reviewDTO.getEventId());
        if(author == null || event == null)
            return new ResponseEntity<>(ResponseWithDTO.create(null, "author or event == null"), HttpStatus.NOT_FOUND);

        List<Review> foundRev = reviewService.findByEventAndAuthor(event,author);
        if(foundRev == null || foundRev.size() != 1)
            return new ResponseEntity<>(ResponseWithDTO.create(null, "Review already exists"), HttpStatus.BAD_REQUEST);

        Review rev = new Review();
        rev.setAuthor(author);
        rev.setEvent(event);
        rev.setContent(reviewDTO.getContent());
        rev.setRating(reviewDTO.getRating());
        rev.setPublicationTime(reviewDTO.getPublicationTime());

        rev = reviewService.save(rev);


        return new ResponseEntity<>(ResponseWithDTO.create(new ReviewDTO(rev), "Review created successfully"), HttpStatus.OK);
    }


    @PutMapping("/reviews/{revId}")
    public ResponseEntity<ResponseWithDTO> editReview(@PathVariable Long revId, //TODO @AuthenticationPrincipal ??
                                                      @RequestBody ReviewDTO reviewDTO) {
        Review oldRev = reviewService.findById(revId);
        if (oldRev == null)
            return new ResponseEntity<>(ResponseWithDTO.create(null, "review to edit not found"), HttpStatus.NOT_FOUND);

        if(reviewDTO.getPublicationTime() != null)
            oldRev.setPublicationTime(reviewDTO.getPublicationTime());
        if(reviewDTO.getRating() != null )
            oldRev.setRating(reviewDTO.getRating());
        if(reviewDTO.getContent() != null)
            oldRev.setContent(reviewDTO.getContent());

        oldRev = reviewService.save(oldRev);
        return new ResponseEntity<>(ResponseWithDTO.create(new ReviewDTO(oldRev), "Review edited successfully"), HttpStatus.OK);
    }

    @GetMapping("reviews/get/{eventId}")
    public ResponseEntity<List<ReviewDTO>> getPagingRevs(@PathVariable Long eventId,
                                                         @RequestParam Integer revsCount,
                                                         @RequestParam Integer pageNumber){
        Event event = eventService.findEventById(eventId);
        if(event == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        List<ReviewDTO> revs = new ArrayList<>(); // TODO как работает страницы и как фильтровать по страницам?
        reviewService.findByEvent(event).forEach(rev->revs.add(new ReviewDTO(rev)));
        return new ResponseEntity<>(revs, HttpStatus.OK);

    }

    @DeleteMapping("reviews/{id}")
    private ResponseEntity<ResponseWithDTO> deleteRev(@PathVariable Long id){

        Review rev = reviewService.findById(id);
        if(rev == null) return  new ResponseEntity<>(ResponseWithDTO.create(null, "nothing to delete"),HttpStatus.OK );

        reviewService.delete(rev);
        return  new ResponseEntity<>(ResponseWithDTO.create(null, "deleted successfully"),HttpStatus.OK );

    }

}
