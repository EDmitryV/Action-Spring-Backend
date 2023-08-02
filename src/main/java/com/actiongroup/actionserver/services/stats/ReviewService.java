package com.actiongroup.actionserver.services.stats;

import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.stats.Review;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.repositories.stats.ReviewRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.sampled.ReverbType;
import java.util.List;

@Data
@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review save(Review review){
        return reviewRepository.save(review);
    }



    public void delete(Review review){
        reviewRepository.delete(review);
    }

    public List<Review> findByAuthor(User author){
        return reviewRepository.findByAuthor(author);
    };
    public List<Review> findByEvent(Event event){
        return findByEvent(event);
    };
    public List<Review> findByEventAndAuthor(Event event, User author){
        return reviewRepository.findByEventAndAuthor(event, author);
    };

    public Review findById(Long id){
        return reviewRepository.findById(id).orElse(null);
    }

}
