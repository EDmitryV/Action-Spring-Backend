package com.actiongroup.actionserver.repositories.stats;

import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.stats.Review;
import com.actiongroup.actionserver.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>{
    public List<Review> findByAuthor(User author);
    public List<Event> findByEvent(Event event);

}
