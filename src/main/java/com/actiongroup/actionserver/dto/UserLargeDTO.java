package com.actiongroup.actionserver.dto;

import com.actiongroup.actionserver.models.users.User;
import lombok.Data;

@Data
public class UserLargeDTO  extends UserSimpleDTO{

    public UserLargeDTO(User user){
        super(user);
    }




}
