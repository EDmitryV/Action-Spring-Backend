package com.actiongroup.actionserver.models.users;

import com.actiongroup.actionserver.models.EntityWithStatus;
import com.actiongroup.actionserver.models.archives.EventsArchive;
import com.actiongroup.actionserver.models.archives.ImageArchive;
import com.actiongroup.actionserver.models.archives.AudioArchive;
import com.actiongroup.actionserver.models.archives.VideoArchive;
import com.actiongroup.actionserver.models.archives.media.Image;
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
    @ManyToOne
    private Image iconImage;

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "owner", fetch = FetchType.EAGER)
    private Set<AudioArchive> audioArchives;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "owner", fetch = FetchType.EAGER)
    private Set<ImageArchive> imageArchives;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "owner", fetch = FetchType.EAGER)
    private Set<VideoArchive> videoArchives;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "owner", fetch = FetchType.EAGER)
    private Set<EventsArchive> eventsArchives;





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