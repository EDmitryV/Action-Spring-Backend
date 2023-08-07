package com.actiongroup.actionserver.services.archives.media;

import com.actiongroup.actionserver.models.archives.ImageArchive;
import com.actiongroup.actionserver.models.archives.media.Image;
import com.actiongroup.actionserver.repositories.archives.media.ImageRepository;
import com.actiongroup.actionserver.services.archives.ImageArchiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImageService {
    private final ImageRepository imageRepository;
    private final MediaService mediaService;
    private final ImageArchiveService imageArchiveService;

    public Image findById(Long id) {
        return imageRepository.findById(id).orElse(null);
    }

    public Resource getImageById(Long id) {
        Image image = imageRepository.findById(id).orElse(null);
        if (image == null)
            throw new IllegalArgumentException("Video with that id doesn't exists: " + id);
        return mediaService.load(image.getOwner(), image.getArchive(), image, MediaTypesPaths.IMAGE);
    }

    public Image save(Image image) {
        return imageRepository.save(image);
    }

    public void saveWithFile(Image image, MultipartFile file) {
        try {
            mediaService.saveFile(file, MediaTypesPaths.IMAGE, image.getOwner(), image.getArchive(), image);
            save(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Image> findByImageArchive(ImageArchive imageArchive) {
        return imageRepository.findByArchive(imageArchive);
    }

    //page starts from 0
    public List<Image> getImagesFromArchive(Long id, int onPage, int page) {
        ImageArchive imageArchive = imageArchiveService.findById(id);
        if (imageArchive == null) {
            throw new IllegalArgumentException("Invalid archive id: " + id);
        }
        //TODO rewrite to load only required from DB (not all)
        List<Image> images = findByImageArchive(imageArchive);
        images = images.subList(page * onPage, Math.min((page + 1) * onPage, images.size()));
        return images;
    }

    public void delete(Image image) {
        image.getArchive().setContentCount(image.getArchive().getContentCount() - 1);
        mediaService.delete(image.getOwner(), image.getArchive(), image, MediaTypesPaths.IMAGE);
        imageRepository.delete(image);
    }

    public boolean existsById(Long imageId) {
        return imageRepository.existsById(imageId);
    }
}
