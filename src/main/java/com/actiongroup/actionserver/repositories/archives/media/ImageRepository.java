package com.actiongroup.actionserver.repositories.archives.media;

import com.actiongroup.actionserver.models.archives.ImageArchive;
import com.actiongroup.actionserver.models.archives.media.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    public List<Image> findByArchive(ImageArchive archive);

}
