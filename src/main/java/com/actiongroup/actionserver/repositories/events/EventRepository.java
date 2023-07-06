package com.actiongroup.actionserver.repositories.events;

import org.springframework.data.jpa.repository.JpaRepository;

import com.actiongroup.actionserver.models.events.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    //TODO add queries for dateTime, author and filters
    @Query(value = "SELECT * FROM event_table e WHERE ST_Within(e.point_column, ST_MakeEnvelope(:minX, :minY, :maxX, :maxY, 4326))", nativeQuery = true)
    List<Event> findEventsOnMap(@Param("minX") double minX, @Param("minY") double minY, @Param("maxX") double maxX, @Param("maxY") double maxY);
}
