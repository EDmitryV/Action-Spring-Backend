package com.actiongroup.actionserver.models.requests;

import lombok.Data;

@Data
public class CreateArchiveRequestBody {
    private String name;
    private String type;
}
