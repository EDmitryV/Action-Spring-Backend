package com.actiongroup.actionserver.repositories.users;

import com.actiongroup.actionserver.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    Optional<User> findByEmail(String username);
    User findByUsernameOrEmail(String username, String email);

    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
}
