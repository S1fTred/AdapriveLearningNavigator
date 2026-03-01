package com.example.adaprivelearningnavigator.security;

import com.example.adaprivelearningnavigator.domain.userPart.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private final Long userId;
    private final String email;

    // Минимально: все авторизованные пользователи — ROLE_USER.
    // Если позже появятся роли (ADMIN), добавим их из БД.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override public String getPassword() { return ""; } // если будет логин по паролю — заполним
    @Override public String getUsername() { return email; }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public static UserPrincipal from(User user) {
        return new UserPrincipal(user.getId(), user.getEmail());
    }
}