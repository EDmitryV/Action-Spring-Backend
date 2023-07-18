package com.actiongroup.actionserver.services.archives;

import com.actiongroup.actionserver.models.archives.EventsArchive;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.repositories.archives.EventArchiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventsArchiveService {
    @Autowired
    private final EventArchiveRepository eventArchiveRepository;

    public EventsArchive findByOwner(User owner){
        return eventArchiveRepository.findByOwner(owner);
    };
    public EventsArchive findByName(String name){
        return eventArchiveRepository.findByName(name);
    };

    public EventsArchive save(EventsArchive arch){
        return eventArchiveRepository.save(arch);
    }

    public void delete(EventsArchive arch){
        eventArchiveRepository.deleteById(arch.getId());
    }
}