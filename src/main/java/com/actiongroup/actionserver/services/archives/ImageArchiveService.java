package com.actiongroup.actionserver.services.archives;

import com.actiongroup.actionserver.models.archives.ImageArchive;
import com.actiongroup.actionserver.models.archives.MusicArchive;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.repositories.archives.ImageArchiveRepository;
import com.actiongroup.actionserver.repositories.archives.MusicArchiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ImageArchiveService {
    @Autowired
    private final ImageArchiveRepository imageArchiveRepository;

    public Set<ImageArchive> findByOwner(User owner){
        return imageArchiveRepository.findByOwner(owner);
    };
    public ImageArchive findByName(String name){
        return imageArchiveRepository.findByName(name);
    };

    public ImageArchive save(ImageArchive arch){
        return imageArchiveRepository.save(arch);
    }

    public void delete(MusicArchive arch){
        imageArchiveRepository.deleteById(arch.getId());
    }
}
