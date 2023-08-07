package com.actiongroup.actionserver.controllers.archives.media;

import com.actiongroup.actionserver.models.archives.ImageArchive;
import com.actiongroup.actionserver.models.archives.media.Audio;
import com.actiongroup.actionserver.models.archives.media.Image;
import com.actiongroup.actionserver.models.dto.ImageDTO;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.services.archives.ImageArchiveService;
import com.actiongroup.actionserver.services.archives.media.ImageService;
import com.actiongroup.actionserver.services.archives.media.MediaService;
import com.actiongroup.actionserver.services.archives.media.MediaTypesPaths;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(value = "/image")
public class ImageController {
    private final MediaService mediaService;
    private final ImageService imageService;
    private final ImageArchiveService imageArchiveService;

    @GetMapping(value = "/get/{id}", produces = "image/jpg")
    public ResponseEntity<Resource> getImage(@PathVariable Long id
    ) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType("image/jpg"))
                .body(imageService.getImageById(id));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/upload")
    public ResponseEntity<ImageDTO> uploadImage(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user,
            @RequestParam("name") String name,
            @RequestParam("archive_id") Long archiveId) throws IOException {
        Image image = new Image();
        image.setName(name != null ? name : image.getId().toString());
        image.setOwner(user);
        ImageArchive imageArchive;
        if (archiveId != null && imageArchiveService.existsById(archiveId)) {
            imageArchive = imageArchiveService.findById(archiveId);
            if (!Objects.equals(imageArchive.getOwner().getId(), user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } else {
            imageArchive = new ImageArchive();
            imageArchive.setOwner(user);
            imageArchiveService.save(imageArchive);
        }
        image.setArchive(imageArchive);
        imageArchive.setContentCount(imageArchive.getContentCount() + 1);
        image = imageService.save(image);
        //TODO maybe remove this and make cascade?
//        imageArchiveService.save(imageArchive);
        mediaService.saveFile(file, MediaTypesPaths.IMAGE, user, imageArchive, image);
        return ResponseEntity.status(HttpStatus.OK).body(new ImageDTO(image));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable Long id, @AuthenticationPrincipal User user){
        Image image = imageService.findById(id);
        if(image == null)
            return ResponseEntity.badRequest().body("Image with id %s doesn't exists".formatted(id));
        if(!Objects.equals(image.getOwner().getId(), user.getId()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User isn't the owner of image");
        imageService.delete(image);
        return ResponseEntity.ok().body("Image is successfully deleted");
    }
}
