package com.actiongroup.actionserver.repositories.user;

import com.actiongroup.actionserver.models.users.User;
import com.actiongroup.actionserver.models.users.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {
    User findByUser(User user);
}
