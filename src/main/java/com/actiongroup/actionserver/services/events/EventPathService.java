package com.actiongroup.actionserver.services.events;

import com.actiongroup.actionserver.models.events.Event;
import com.actiongroup.actionserver.models.events.EventPath;
import com.actiongroup.actionserver.models.events.PathCheckpoint;
import com.actiongroup.actionserver.repositories.events.EventPathRepository;
import com.actiongroup.actionserver.repositories.events.PathCheckpointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventPathService {
    private EventPathRepository eventPathRepository;

    private PathCheckpointRepository pathCheckpointRepository;
    @Autowired
    public EventPathService(EventPathRepository eventPathRepository, PathCheckpointRepository pathCheckpointRepository) {
        this.eventPathRepository = eventPathRepository;
        this.pathCheckpointRepository = pathCheckpointRepository;
    }


    public EventPath saveEventPath(EventPath path){
        return eventPathRepository.save(path);
    }

    public PathCheckpoint addEventToPath(Event event, Integer index, EventPath targetPath){
        PathCheckpoint checkpoint = targetPath.eventToCheckpoint(event);
        return addCheckpointToPath(checkpoint, index,targetPath);
    }
    public PathCheckpoint addEventToPath(Event event, EventPath targetPath){
        PathCheckpoint checkpoint = targetPath.eventToCheckpoint(event);
        return addCheckpointToPath(checkpoint,targetPath);
    }


    private PathCheckpoint addCheckpointToPath(PathCheckpoint checkpoint, Integer index, EventPath targetPath){
        List<PathCheckpoint> checkpoints = findCheckpointsByEventPath(targetPath);
        if(index > checkpoints.size() || index < 0) return null;
        if(index == checkpoints.size()){
            checkpoint.setIndex(checkpoints.size());
            return pathCheckpointRepository.save(checkpoint);
        }
        if(checkpoints == null) checkpoints = new ArrayList<>();
        checkpoint.setIndex(index);
        checkpoints.add(index, checkpoint);
        for(int i = 0 ;i< checkpoints.size();i++) {
            var chp =  checkpoints.get(i);
            chp.setIndex(i);
            chp = pathCheckpointRepository.save(chp);
            checkpoints.set(i, chp);
        }
        return checkpoints.get(index);
    }
    private PathCheckpoint addCheckpointToPath(PathCheckpoint checkpoint, EventPath targetPath){
        List<PathCheckpoint> checkpoints = findCheckpointsByEventPath(targetPath);
        if(checkpoints == null) checkpoints = new ArrayList<>();

        checkpoint.setIndex(checkpoints.size());
        return pathCheckpointRepository.save(checkpoint);
    }

    public List<PathCheckpoint> findCheckpointsByEventPath(EventPath path){
        return pathCheckpointRepository.findByParentPathOrderByIndex(path).orElse(null);
    }
    public List<PathCheckpoint> findCheckpointsByEvent(Event event){
        return pathCheckpointRepository.findByEvent(event).orElse(null);
    }

    public void deletePath(EventPath path){
        List<PathCheckpoint> checkpoints = findCheckpointsByEventPath(path);
        for(PathCheckpoint checkpoint: checkpoints){
            deleteCheckPoint(checkpoint);
        }
        pathCheckpointRepository.deleteById(path.getId());
    }

    public void deleteCheckpointsByEvent(Event event){
        List<PathCheckpoint> checkpoints = findCheckpointsByEvent(event);
        for(PathCheckpoint checkpoint: checkpoints){
            deleteCheckPoint(checkpoint);
        }
    }

    public void deleteCheckPoint(PathCheckpoint checkpoint){
        pathCheckpointRepository.deleteById(checkpoint.getId());
    }

    public List<Event> getEventsInPath(EventPath path){
        List<PathCheckpoint> checkpoints = findCheckpointsByEventPath(path);
        if(checkpoints == null) return null;
        List<Event> result = new ArrayList<>();
        checkpoints.forEach(checkpoint -> result.add(checkpoint.getEvent()));
        return result;
    }
}
