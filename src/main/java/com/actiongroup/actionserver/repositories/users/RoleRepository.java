package com.actiongroup.actionserver.repositories.users;

import com.actiongroup.actionserver.models.users.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository /*extends JpaRepository<Role, Long>*/ {
    Optional<Role> findByName(String name);
}
