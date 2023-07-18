package com.actiongroup.actionserver.services.archives.media;

import com.actiongroup.actionserver.models.archives.VideoArchive;
import com.actiongroup.actionserver.models.archives.media.Video;
import com.actiongroup.actionserver.models.dto.VideoDTO;
import com.actiongroup.actionserver.repositories.archives.media.VideoRepository;
import com.actiongroup.actionserver.services.archives.VideoArchiveService;
import com.actiongroup.actionserver.services.users.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VideoService {
    private final ResourceLoader resourceLoader;
    private final VideoRepository videoRepository;
    private final VideoArchiveService videoArchiveService;
    private final MediaStorageService mediaStorageService;

    public Mono<Resource> getVideoById(Long id) {
        //TODO add not found error
        Video video = videoRepository.findById(id).orElse(null);
        if (video == null)
            throw new IllegalArgumentException("Video with that id doesn't exists: " + id);
        return Mono.fromSupplier(() -> this.resourceLoader.getResource("classpath:" + mediaStorageService.getFilepath(video.getOwner(), video.getVideoArchive(), video, MediaTypesPaths.VIDEO)));
    }

    public Video saveVideoEntity(Video video) {
        return videoRepository.save(video);
    }

    public List<Video> getVideoEntitiesFromArchive(VideoArchive videoArchive) {
        return videoRepository.findByVideoArchive(videoArchive);
    }

    public Flux<VideoDTO> getVideosFromArchive(Long id) {
        VideoArchive archive = videoArchiveService.findById(id);
        if (archive == null) {
            throw new IllegalArgumentException("Invalid archive id: " + id);
        }
        List<Video> videos = getVideoEntitiesFromArchive(archive);
        Video[] videosArray = new Video[videos.size()];
        videos.toArray(videosArray);
        if (videos.size() == 0) {
            return Flux.empty();
        }
        return Flux.fromArray(videosArray)
                .flatMap(this::extractDTOForVideo);
    }

    private Mono<VideoDTO> extractDTOForVideo(Video video) {
        File videoFile = mediaStorageService.getFilepath(video.getOwner(), video.getVideoArchive(), video, MediaTypesPaths.VIDEO).toFile();
        return readFirstFrameFromVideo(videoFile)
                .map(firstFrame -> {
                    try (FileInputStream inputStream = new FileInputStream(videoFile)) {
                        VideoDTO videoDTO = new VideoDTO();
                        videoDTO.setId(video.getId());
                        videoDTO.setName(video.getName());
                        videoDTO.setAutoRepeat(video.getAutoRepeat());
                        videoDTO.setFirstFrame(firstFrame);
                        videoDTO.setFileSize(inputStream.available());
                        videoDTO.setArchiveId(video.getVideoArchive().getId());
                        videoDTO.setAuthorId(video.getOwner().getId());
                        return videoDTO;
                    } catch (IOException e) {
                        throw new RuntimeException("Error reading video file: " + e.getMessage());
                    }
                });
    }

    private Mono<BufferedImage> readFirstFrameFromVideo(File videoFile) {
        return Mono.fromCallable(() -> {
            try {
                BufferedImage firstFrame = null;

                // Use Java Image I/O API to read the first frame from the video
                ImageReader reader = ImageIO.getImageReadersByFormatName("mp4").next();
                ImageInputStream imageInputStream = ImageIO.createImageInputStream(videoFile);
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
        });
    }
}
