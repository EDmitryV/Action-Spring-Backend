package com.actiongroup.actionserver.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointDTO {
    private double latitude;
    private double longitude;

    PointDTO() {
    }

    PointDTO(double lng, double lat) {
        latitude = lat;
        longitude = lng;
    }

//    PointDTO(String pointJson) {
//        var pointArgs = pointJson.split("\"");
//        for (var i = 0; i < pointArgs.length; i++) {
//            if (pointArgs[i] == "latitude") {
//                latitude = Double.parseDouble(pointArgs[i + 2]);
//            } else if (pointArgs[i] == "longitude") {
//                longitude = Double.parseDouble(pointArgs[i + 2]);
//            }
//        }
//    }
}
