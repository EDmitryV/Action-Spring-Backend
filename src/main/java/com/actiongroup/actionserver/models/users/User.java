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
import java.util.Set;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends EntityWithStatus implements UserDetails{

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

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany
    @JoinColumn(name = "friend_id", referencedColumnName = "id")
    private Set<User> Friends;

    @ManyToMany
    @JoinColumn(name = "subscription_id", referencedColumnName = "id")
    private Set<User> Subscriptions;

    @ManyToMany
    @JoinColumn(name = "blocked_user_id", referencedColumnName = "id")
    private Set<User> BlackList;


    @ManyToMany
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private Set<Chat> chats;

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