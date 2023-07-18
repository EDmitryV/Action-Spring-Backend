package com.actiongroup.actionserver.services.archives;

import com.actiongroup.actionserver.models.archives.MusicArchive;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.repositories.archives.MusicArchiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MusicArchiveService {

    @Autowired
    private final MusicArchiveRepository musicArchiveRepository;

    public MusicArchive findByOwner(User owner){
        return musicArchiveRepository.findByOwner(owner);
    };
    public MusicArchive findByName(String name){
        return musicArchiveRepository.findByName(name);
    };

    public MusicArchive save(MusicArchive arch){
        return musicArchiveRepository.save(arch);
    }

    public void delete(MusicArchive arch){
        musicArchiveRepository.deleteById(arch.getId());
    }

}
