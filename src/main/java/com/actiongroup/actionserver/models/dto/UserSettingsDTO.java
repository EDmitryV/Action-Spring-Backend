package com.actiongroup.actionserver.models.dto;

import com.actiongroup.actionserver.models.users.UserSettings;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class UserSettingsDTO {
    private long id;
    private boolean isVerified;

    public UserSettingsDTO(UserSettings userSettings){
        id = userSettings.getId();
        isVerified = userSettings.isVerified();
    }
}
