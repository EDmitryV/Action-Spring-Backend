package com.actiongroup.actionserver.services.archives.media;

import com.actiongroup.actionserver.models.archives.VideoArchive;
import com.actiongroup.actionserver.models.archives.media.Video;
import com.actiongroup.actionserver.repositories.archives.media.VideoRepository;
import com.actiongroup.actionserver.services.archives.VideoArchiveService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VideoService {
    private final VideoArchiveService videoArchiveService;
    private final VideoRepository videoRepository;
    private final MediaService mediaService;

    public Resource getVideoById(Long id) {
        Video video = videoRepository.findById(id).orElse(null);
        if (video == null)
            throw new IllegalArgumentException("Video with that id doesn't exists: " + id);
        return mediaService.load(video.getOwner(), video.getArchive(), video, MediaTypesPaths.VIDEO);
    }

    public Video save(Video video) {
        return videoRepository.save(video);
    }

    public void saveWithFile(Video video, MultipartFile file) {
        try {
            mediaService.saveFile(file, MediaTypesPaths.VIDEO, video.getOwner(), video.getArchive(), video);
            save(video);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Video> findByVideoArchive(VideoArchive videoArchive) {
        return videoRepository.findByArchive(videoArchive);
    }

    //page starts from 0
    public List<Video> getVideosFromArchive(Long id, int onPage, int page) {
        VideoArchive videoArchive = videoArchiveService.findById(id);
        if (videoArchive == null) {
            throw new IllegalArgumentException("Invalid archive id: " + id);
        }
        //TODO rewrite to load only required from DB (not all)
        List<Video> videos = findByVideoArchive(videoArchive);
        return videos.subList(page * onPage, (page + 1) * onPage);
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

    public void delete(Video video) {
        video.getArchive().setContentCount(video.getArchive().getContentCount() - 1);
        mediaService.delete(video.getOwner(), video.getArchive(), video, MediaTypesPaths.VIDEO);
        videoRepository.delete(video);
    }

    public Video findById(Long id) {
        return videoRepository.findById(id).orElse(null);
    }
}
