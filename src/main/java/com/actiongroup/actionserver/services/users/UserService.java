package com.actiongroup.actionserver.services.users;


import com.actiongroup.actionserver.models.users.Role;
import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.models.users.UserSettings;
import com.actiongroup.actionserver.repositories.users.RoleRepository;
import com.actiongroup.actionserver.repositories.users.UserRepository;
import com.actiongroup.actionserver.repositories.users.UserSettingsRepository;
import com.actiongroup.actionserver.repositories.users.UserRepository;
import com.actiongroup.actionserver.repositories.users.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.actiongroup.actionserver.repositories.user.UserRelationRepository;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepo;
    private final UserSettingsRepository settingsRepo;
    private final UserRelationRepository relationRepository;


    public User save(User user) {
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
        //deleteUserSettings(settingsRepo.findByUser(user).orElse(null));
        relationRepository.deleteAll(relationRepository.findBySourceUserOrTargetUser(user,user));
        if(user!=null)
            userRepo.deleteById(user.getId());

    }

    private void deleteUserSettings(UserSettings settings){
        settingsRepo.deleteById(settings.getId());
    }

    public User findByUsername(String username){
        return userRepo.findByUsername(username).orElse(null);
    }

    public User findByEmail(String email){
        return userRepo.findByEmail(email).orElse(null);
    }
    public List<User> findByUsernameContaining(String name){
        return userRepo.findByUsernameContaining(name);
    }

    public User findById(Long id){
        return userRepo.findById(id).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username).orElse(null);
    }
}
