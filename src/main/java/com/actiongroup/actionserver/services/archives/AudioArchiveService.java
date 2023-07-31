package com.actiongroup.actionserver.services.archives;

import com.actiongroup.actionserver.models.archives.AudioArchive;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.repositories.archives.AudioArchiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AudioArchiveService {

    @Autowired
    private final AudioArchiveRepository audioArchiveRepository;

    public Set<AudioArchive> findByOwner(User owner){
        return audioArchiveRepository.findByOwner(owner);
    };
    public AudioArchive findByName(String name){
        return audioArchiveRepository.findByName(name);
    };

    public AudioArchive save(AudioArchive arch){
        return audioArchiveRepository.save(arch);
    }

    public void delete(AudioArchive arch){
        audioArchiveRepository.deleteById(arch.getId());
    }

    public AudioArchive findById(Long id) {
        return audioArchiveRepository.findById(id).orElse(null);
    }

    public boolean existsById(Long id) {
        return audioArchiveRepository.existsById(id);
    }
}
