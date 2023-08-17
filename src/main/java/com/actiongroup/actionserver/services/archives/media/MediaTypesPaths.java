package com.actiongroup.actionserver.services.archives.media;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MediaTypesPaths {
    ROOT("data"),
    AUDIO("data/audio"),
    VIDEO("data/video"),
    IMAGE("data/image");
    @Getter
    private final String path;
}