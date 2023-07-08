package com.actiongroup.actionserver.repositories.stats;

import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.stats.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticsRepository extends JpaRepository<Statistics,Long> {
    public Statistics findByEvent(Event event);

}
