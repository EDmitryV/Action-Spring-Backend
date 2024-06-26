package com.actiongroup.actionserver.repositories.stats;

import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.stats.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatisticsRepository extends JpaRepository<Statistics,Long> {
    public Statistics findByEvent(Event event);

}
