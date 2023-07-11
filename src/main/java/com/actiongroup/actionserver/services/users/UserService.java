package com.actiongroup.actionserver.services.users;


import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.models.users.UserSettings;
import com.actiongroup.actionserver.repositories.users.RoleRepository;
import com.actiongroup.actionserver.repositories.users.UserRepository;
import com.actiongroup.actionserver.repositories.users.UserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.actiongroup.actionserver.repositories.user.UserRelationRepository;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepo;
    //@Autowired
    //RoleRepository roleRepo;

    @Autowired
    UserSettingsRepository settingsRepo;
    @Autowired
    UserRelationRepository relationRepository;

    public User save(User user) {
        if(userRepo.existsByUsername(user.getUsername())) return null;

        //User userFromDB = userRepo.findByUsername(user.getUsername());
//        if(user.getRoles() == null || user.getRoles().size()==0){
//            Role roles = roleRepo.findByName("ROLE_USER").get();
//            user.setRoles(Collections.singleton(roles));
//        }


        User saveduser = userRepo.save(user);

        UserSettings settings = settingsRepo.findByUser(user);
        if(settings == null)
            settings = new UserSettings();
        settings.setVerified(false);
        settings.setUser(user);

        settingsRepo.save(settings);
        return saveduser;
    }

    public boolean existsByUsername(String username){
        return userRepo.existsByUsername(username);
    }

    public boolean existsByEmail(String email){
        return userRepo.existsByEmail(email);
    }

    public void deleteUser(User user){
        deleteUserSettings(settingsRepo.findByUser(user));
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

    public Optional<User> findByEmail(String email){
        return userRepo.findByEmail(email);
    }

    public User findById(Long id){
        return userRepo.findById(id).orElse(null);
    }

    public UserSettings getSettingsByUser(User user){
        return settingsRepo.findByUser(user);
    }
}
