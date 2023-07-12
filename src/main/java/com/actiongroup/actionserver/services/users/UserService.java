package com.actiongroup.actionserver.services.users;


import com.actiongroup.actionserver.models.users.Role;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.models.users.UserSettings;
import com.actiongroup.actionserver.repositories.users.UserRepository;
import com.actiongroup.actionserver.repositories.users.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.actiongroup.actionserver.repositories.user.UserRelationRepository;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class UserService {
    private final UserRepository userRepo;
    private final UserSettingsRepository settingsRepo;
    private final UserRelationRepository relationRepository;

    public User save(User user) {
      //TODO check that another user with same email can save in db
        if(user.getRole() == null){
            user.setRole(Role.USER);
        }
        if (user.getSettings() == null){
            UserSettings settings = new UserSettings();
            settings.setVerified(false);
            user.setSettings(settings);
            settingsRepo.save(settings);
        }
        return userRepo.save(user);

    }

    public boolean existsByUsername(String username){
        return userRepo.existsByUsername(username);
    }

    public boolean existsByEmail(String email){
        return userRepo.existsByEmail(email);
    }

    public void deleteUser(User user){
        relationRepository.deleteAll(relationRepository.findBySourceUserOrTargetUser(user,user));
        if(user!=null)
            userRepo.deleteById(user.getId());

    }

    private void deleteUserSettings(UserSettings settings){
        settingsRepo.deleteById(settings.getId());
    }

    public User findByUsername(String username){
        return userRepo.findByUsername(username);
    }


    public User findByEmail(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }
    public User findById(Long id){
        return userRepo.findById(id).orElse(null);
    }
}
