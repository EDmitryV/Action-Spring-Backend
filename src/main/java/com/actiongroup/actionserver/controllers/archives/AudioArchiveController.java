package com.actiongroup.actionserver.controllers.archives;

import com.actiongroup.actionserver.models.archives.media.Audio;
import com.actiongroup.actionserver.models.archives.media.Image;
import com.actiongroup.actionserver.models.dto.ArchiveDTO;
import com.actiongroup.actionserver.models.dto.AudioDTO;
import com.actiongroup.actionserver.models.dto.ImageDTO;
import com.actiongroup.actionserver.services.archives.AudioArchiveService;
import com.actiongroup.actionserver.services.archives.media.AudioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(value = "/audio-archive")
public class AudioArchiveController {
    private final AudioService audioService;
    private final AudioArchiveService audioArchiveService;

    @GetMapping("/content/get/{id}")
    public ResponseEntity<List<AudioDTO>> getArchiveContent(@PathVariable Long id, @RequestParam("on_page") int onPage, @RequestParam("page") int page) {
        List<Audio> audios = audioService.getAudioFromArchive(id, onPage, page);
        List<AudioDTO> audioDTOs = new ArrayList<>();
        for (Audio audio : audios) {
            audioDTOs.add(new AudioDTO(audio));
        }
        return ResponseEntity
                .ok()
                .body(audioDTOs);
    }

    @GetMapping("/info/get/{id}")
    public ResponseEntity<ArchiveDTO> getArchiveMetaData(@PathVariable Long id) {
        return ResponseEntity
                .ok()
                .body(new ArchiveDTO(audioArchiveService.findById(id)));
    }
}
