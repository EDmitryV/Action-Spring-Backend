package com.actiongroup.actionserver.services.archives.media;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.actiongroup.actionserver.models.archives.Archive;
import com.actiongroup.actionserver.models.archives.media.Media;
import com.actiongroup.actionserver.models.users.User;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//TODO добавить ограничения на размер файла
@Service
public class MediaStorageService {

    public void init() {
        try {
            for (MediaTypesPaths path : MediaTypesPaths.values()) {
                Files.createDirectories(path.getPath());
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
        return type.getPath().resolve("%s/%s/%s%s".formatted(author.getId(), archive.getId(), media.getId(), ext));
    }

    public Mono<Void> save(Mono<FilePart> filePartMono, MediaTypesPaths type, User author, Archive archive, Media media) {
        return filePartMono.doOnNext(fp -> System.out.println("Receiving File:" + fp.filename()))
                .flatMap(filePart -> filePart.transferTo(getFilepath(author, archive, media, type)))
                .then();
    }

    public Flux<DataBuffer> load(MediaTypesPaths type, User author, Archive archive, Media media) {
        try {
            Resource resource = new UrlResource(getFilepath(author, archive, media, type).toUri());

            if (resource.exists() || resource.isReadable()) {
                return DataBufferUtils.read(resource, new DefaultDataBufferFactory(), 4096);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}