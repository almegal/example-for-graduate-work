package ru.skypro.homework.service.impl;

import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.entity.User;

@RequiredArgsConstructor
public class UserSecurityDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * @return true, если не истек срок действия аккаунта, иначе - false
     */

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * @return true, если аккаунт не заблокирован, иначе - false
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * @return true, если не истек срок действия учетных данных, иначе - false
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return true, если есть доступ, иначе - false
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}

