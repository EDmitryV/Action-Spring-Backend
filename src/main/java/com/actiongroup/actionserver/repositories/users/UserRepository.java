package com.actiongroup.actionserver.repositories.users;

import com.actiongroup.actionserver.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String username);
    Optional<User> findByUsernameOrEmail(String username, String email);
    List<User> findByUsernameContaining(String name);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
}
