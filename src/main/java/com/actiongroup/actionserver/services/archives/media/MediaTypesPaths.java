package com.actiongroup.actionserver.services.archives.media;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
public enum MediaTypesPaths {
    ROOT(Paths.get("data")),
    MUSIC(Paths.get("data/music")),
    VIDEO(Paths.get("data/video")),
    IMAGE(Paths.get("data/image"));
    @Getter
    private final Path path;
}