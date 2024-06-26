package com.actiongroup.actionserver.repositories.events;

import com.actiongroup.actionserver.models.archives.Archive;
import org.springframework.data.jpa.repository.JpaRepository;

import com.actiongroup.actionserver.models.events.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    //TODO add right queries for dateTime, author and filters
//    @Query(value = "SELECT * FROM event e WHERE ST_Within(e.point, ST_MakeEnvelope(:minX, :minY, :maxX, :maxY, 4326))", nativeQuery = true)
//    List<Event> findEventsOnMap(@Param("minX") double minX, @Param("minY") double minY, @Param("maxX") double maxX, @Param("maxY") double maxY);


    @Query(value = "SELECT * FROM event e WHERE ST_Within(e.point, ST_MakeEnvelope(:minX, :minY, :maxX, :maxY, 4326)) AND e.starts_at <= :endsAt AND e.ends_at >= :startsAt",
            nativeQuery = true)
    List<Event> findEventsOnMapWithTimeRange(
            @Param("minX") double minX, @Param("minY") double minY,
            @Param("maxX") double maxX, @Param("maxY") double maxY,
            @Param("startsAt") LocalDateTime startsAt, @Param("endsAt") LocalDateTime endsAt);
    List<Event> findByArchive(Archive archive);
}
