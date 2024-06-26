package com.actiongroup.actionserver.repositories.events;

import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.events.EventPath;
import com.actiongroup.actionserver.models.events.PathCheckpoint;
import jdk.dynalink.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PathCheckpointRepository extends JpaRepository<PathCheckpoint, Long> {
    public Optional<List<PathCheckpoint>> findByParentPathOrderByIndex(EventPath path);
    public Optional<List<PathCheckpoint>> findByEvent(Event event);

}
