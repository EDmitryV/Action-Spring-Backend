package com.actiongroup.actionserver.services.archives.media;

import com.actiongroup.actionserver.models.archives.AudioArchive;
import com.actiongroup.actionserver.models.archives.media.Audio;
import com.actiongroup.actionserver.repositories.archives.media.AudioRepository;
import com.actiongroup.actionserver.services.archives.AudioArchiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AudioService {
    private final AudioRepository audioRepository;
    private final AudioArchiveService audioArchiveService;
    private final MediaService mediaService;
public Audio findById(Long id){
    return audioRepository.findById(id).orElse(null);
}
    public Resource getAudioById(Long id) {
        Audio audio = audioRepository.findById(id).orElse(null);
        if (audio == null)
            throw new IllegalArgumentException("Audio file with that id doesn't exists: " + id);
        return mediaService.load(audio.getOwner(), audio.getArchive(), audio, MediaTypesPaths.MUSIC);
    }

    public Audio save(Audio audio) {
        return audioRepository.save(audio);
    }

    public List<Audio> findByAudioArchive(AudioArchive audioArchive) {
        return audioRepository.findByArchive(audioArchive);
    }

    //page starts from 0
    public List<Audio> getAudioFromArchive(Long id, int onPage, int page) {
        AudioArchive audioArchive = audioArchiveService.findById(id);
        if (audioArchive == null) {
            throw new IllegalArgumentException("Invalid archive id: " + id);
        }
        //TODO rewrite to load only required from DB (not all)
        List<Audio> audios = findByAudioArchive(audioArchive);
        return audios.subList(page * onPage, (page + 1) * onPage);
    }

    public void delete(Audio audio) {
        //TODO check for work
        audio.getArchive().setContentCount(audio.getArchive().getContentCount() - 1);
        mediaService.delete(audio.getOwner(), audio.getArchive(), audio, MediaTypesPaths.MUSIC);
        audioRepository.delete(audio);
    }
}
