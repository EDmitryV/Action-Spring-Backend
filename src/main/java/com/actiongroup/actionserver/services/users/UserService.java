package com.actiongroup.actionserver.services.users;


import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.models.users.UserSettings;
import com.actiongroup.actionserver.repositories.users.UserRepository;
import com.actiongroup.actionserver.repositories.users.UserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepo;

    @Autowired
    UserSettingsRepository settingsRepo;

//    public boolean save(User user) {
//        User userFromDB = userRepo.findByUsername(user.getUsername());
//
//        if (userFromDB != null) {
//            return false;
//        }
//
//        if(user.getRoles() == null || user.getRoles().size()==0){
//            Role roles = roleRepo.findByName("ROLE_USER").get();
//            user.setRoles(Collections.singleton(roles));
//        }
//        UserSettings settings = new UserSettings();
//        settings.setVerified(false);
//        settings.setUser(user);
//
//
//        userRepo.save(user);
//        settingsRepo.save(settings);
//        return true;
//    }

    public boolean existsByUsername(String username){
        return userRepo.existsByUsername(username);
    }

    public boolean existsByEmail(String email){
        return userRepo.existsByEmail(email);
    }

    public void deleteUser(User user){
        deleteUserSettings(settingsRepo.findByUser(user));
        userRepo.deleteById(user.getId());
    }

    private void deleteUserSettings(UserSettings settings){
        settingsRepo.deleteById(settings.getId());
    }

    public User findByUsername(String username){
        return userRepo.findByUsername(username);
    }

    public User findByEmail(String email){
        return userRepo.findByEmail(email).orElse(null);
    }

    public UserSettings getSettingsByUser(User user){
        return settingsRepo.findByUser(user);
    }


//    @Override
//    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepo.findByUsername(username);
//        if (user == null) {
//            throw new UsernameNotFoundException("User not exists by Username");
//        }
//
//        Set<GrantedAuthority> authorities = user.ge().stream()
//                .map((role) -> new SimpleGrantedAuthority(role.getName()))
//                .collect(Collectors.toSet());
//
//        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
//    }

}
