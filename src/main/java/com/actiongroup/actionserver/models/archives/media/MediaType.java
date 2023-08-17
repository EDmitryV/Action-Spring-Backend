package com.actiongroup.actionserver.models.archives.media;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MediaType {

    Audio("audio"),
    Video("video"),
    Event("event"),
    Image("image");
    @Getter
    private final String mediaType;
}