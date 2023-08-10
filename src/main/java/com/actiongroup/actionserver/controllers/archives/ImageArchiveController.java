package com.actiongroup.actionserver.controllers.archives;

import com.actiongroup.actionserver.models.archives.media.Image;
import com.actiongroup.actionserver.models.dto.ArchiveDTO;
import com.actiongroup.actionserver.models.dto.ImageDTO;
import com.actiongroup.actionserver.services.archives.ImageArchiveService;
import com.actiongroup.actionserver.services.archives.media.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(value = "/image-archive")
public class ImageArchiveController {
    private final ImageService imageService;
    private final ImageArchiveService imageArchiveService;


//    @GetMapping("/get/{id}")
//    public ResponseEntity<List<ImageDTO>> getArchiveContent(@PathVariable Long id, @RequestParam("on_page") int onPage, @RequestParam("page") int page) {
//        List<Image> images = imageService.getImagesFromArchive(id, onPage, page);
//        List<ImageDTO> imageDTOs = new ArrayList<>();
//        for (Image image : images) {
//            imageDTOs.add(new ImageDTO(image));
//        }
//        return ResponseEntity
//                .ok()
//                .body(imageDTOs);
//    }
//
//    @GetMapping("/info/get/{id}")
//    public ResponseEntity<ArchiveDTO> getArchiveMetaData(@PathVariable Long id) {
//        return ResponseEntity
//                .ok()
//                .body(new ArchiveDTO(imageArchiveService.findById(id)));
//    }
}
