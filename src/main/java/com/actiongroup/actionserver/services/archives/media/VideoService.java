package com.actiongroup.actionserver.services.archives.media;

import com.actiongroup.actionserver.models.archives.VideoArchive;
import com.actiongroup.actionserver.models.archives.media.Video;
import com.actiongroup.actionserver.models.dto.VideoDTO;
import com.actiongroup.actionserver.repositories.archives.media.VideoRepository;
import com.actiongroup.actionserver.services.archives.VideoArchiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VideoService {
    private final ResourceLoader resourceLoader;
    private final VideoRepository videoRepository;
    private final VideoArchiveService videoArchiveService;
    private final MediaService mediaService;

    public Resource getVideoById(Long id) {
        Video video = videoRepository.findById(id).orElse(null);
        if (video == null)
            throw new IllegalArgumentException("Video with that id doesn't exists: " + id);
        return mediaService.load(video.getOwner(), video.getVideoArchive(), video, MediaTypesPaths.VIDEO);
    }

    public Video save(Video video) {
        return videoRepository.save(video);
    }

    public List<Video> findByVideoArchive(VideoArchive videoArchive) {
        return videoRepository.findByVideoArchive(videoArchive);
    }

    //page starts from 0
    public List<VideoDTO> getVideosFromArchive(Long id, int onPage, int page) {
        VideoArchive videoArchive = videoArchiveService.findById(id);
        if (videoArchive == null) {
            throw new IllegalArgumentException("Invalid archive id: " + id);
        }
        //TODO rewrite to load only required from DB (not all)
        List<Video> videos = findByVideoArchive(videoArchive);
        videos = videos.subList(page * onPage, (page + 1) * onPage);
        List<VideoDTO> videoDTOs = new ArrayList<>();
        for (Video video : videos) {
            File file = mediaService.getFilepath(video.getOwner(), video.getVideoArchive(), video, MediaTypesPaths.VIDEO).toFile();
            try (FileInputStream inputStream = new FileInputStream(file)) {
                videoDTOs.add(new VideoDTO(
                        video.getId(),
                        video.getName(),
                        video.getAutoRepeat(),
                        readFirstFrameFromVideo(file),
                        inputStream.available(),
                        video.getOwner().getId(),
                        video.getVideoArchive().getId()
                ));
            } catch (IOException e) {
                throw new RuntimeException("Error reading video file: " + e.getMessage());
            }
        }
        return videoDTOs;
    }

    private BufferedImage readFirstFrameFromVideo(File file) {
        try {
            BufferedImage firstFrame = null;

            // Use Java Image I/O API to read the first frame from the video
            ImageReader reader = ImageIO.getImageReadersByFormatName("mp4").next();
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(file);
            reader.setInput(imageInputStream);

            if (reader.getNumImages(true) > 0) {
                firstFrame = reader.read(0);
            }

            reader.dispose();
            imageInputStream.close();

            return firstFrame;
        } catch (Exception e) {
            throw new RuntimeException("Error while processing the video: " + e.getMessage());
        }
    }
    public void delete(Video video){
        mediaService.delete(video.getOwner(), video.getVideoArchive(), video, MediaTypesPaths.VIDEO);
    }
}
