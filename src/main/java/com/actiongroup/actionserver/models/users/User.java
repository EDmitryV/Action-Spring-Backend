package com.actiongroup.actionserver.models.users;

import com.actiongroup.actionserver.models.EntityWithStatus;
import com.actiongroup.actionserver.models.chats.Chat;
import com.actiongroup.actionserver.models.auth.Token;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends EntityWithStatus implements UserDetails {

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private LocalDate birthDate;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.REFRESH})
    private List<Token> tokens;

    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.REFRESH})
    private UserSettings settings;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private Set<Chat> chats = new HashSet<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    //TODO make it work normal
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}