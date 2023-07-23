package com.actiongroup.actionserver.services.archives.media;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.actiongroup.actionserver.models.archives.Archive;
import com.actiongroup.actionserver.models.archives.media.Media;
import com.actiongroup.actionserver.models.users.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

//TODO добавить ограничения на размер файла
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MediaService {
    private final HttpServletRequest request;
    private final ResourceLoader resourceLoader;

    public void init() {
        try {
            for (MediaTypesPaths path : MediaTypesPaths.values()) {
                Files.createDirectories(Paths.get(path.getPath()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public Path getFilepath(User author, Archive archive, Media media, MediaTypesPaths type) {
        String ext;
        switch (type) {
            case IMAGE -> ext = ".jpg";
            case MUSIC -> ext = ".mp3";
            case VIDEO -> ext = ".mp4";
            default -> ext = ".txt";
        }
        return Path.of("%s/%s/%s/%s/%s%s".formatted(getResourcePath(), type.getPath(), author.getId(), archive.getId(), media.getId(), ext));
    }

    public void prepareFilePath(Path pathToFile) {
        try {
            Files.createDirectories(pathToFile.getParent());
            System.out.println("Folders created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to create folders.");
        }
    }

    public String getResourcePath() {
        Resource resource = resourceLoader.getResource("classpath:");
        String path = null;
        try {
            path = resource.getFile().getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    public void saveFile(MultipartFile file, MediaTypesPaths type, User author, Archive archive, Media media) throws IOException {
        try {
            Path path = getFilepath(author, archive, media, type);
            prepareFilePath(path);
            file.transferTo(new File(path.toString()));
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }


    public Resource load(User author, Archive archive, Media media, MediaTypesPaths type) {
        try {
            Resource resource = new FileSystemResource(getFilepath(author, archive, media, type));

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void delete(User author, Archive archive, Media media, MediaTypesPaths type){
        Path path = getFilepath(author, archive, media, type);
        try {
            Files.deleteIfExists(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}