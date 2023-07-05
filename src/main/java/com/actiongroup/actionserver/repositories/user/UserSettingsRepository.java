package com.actiongroup.actionserver.repositories.user;

import com.actiongroup.actionserver.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingsRepository extends JpaRepository<User, Long> {
    User findByUser(User user);
}
