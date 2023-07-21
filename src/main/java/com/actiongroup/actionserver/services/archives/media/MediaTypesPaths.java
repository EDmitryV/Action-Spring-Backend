package com.actiongroup.actionserver.services.archives.media;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
public enum MediaTypesPaths {
    ROOT("data"),
    MUSIC("data/music"),
    VIDEO("data/video"),
    IMAGE("data/image");
    @Getter
    private final String path;
}