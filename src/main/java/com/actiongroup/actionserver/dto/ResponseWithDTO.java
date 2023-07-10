package com.actiongroup.actionserver.dto;

import lombok.Data;

@Data
public class ResponseWithDTO<DTO extends ApiDto> {
    private String message;
    private DTO data;

    public static <DTO extends ApiDto> ResponseWithDTO create(DTO obj, String msg){
        ResponseWithDTO<DTO> resp = new ResponseWithDTO<>();
        resp.setMessage(msg);
        resp.setData(obj);

        return resp;
    }
}
