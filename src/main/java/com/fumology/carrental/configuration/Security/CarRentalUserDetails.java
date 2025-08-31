package com.fumology.carrental.configuration.Security;

import com.fumology.carrental.database.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CarRentalUserDetails implements UserDetails {

    private final String email;
    private final String password;
    private final List<SimpleGrantedAuthority> roles;

    public CarRentalUserDetails(UserEntity user) {
        email = user.getEmail();
        password = user.getPassword();
        roles = user.getRoles().stream().map(
                role -> new SimpleGrantedAuthority(role.getRole().name()))
                .toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
