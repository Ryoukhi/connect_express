package com.eadl.connect_backend.infrastructure.config.security;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.eadl.connect_backend.domain.model.user.User;

import io.jsonwebtoken.lang.Collections;
import lombok.Data;

@Data
public class CustomUserDetails implements UserDetails{

    protected Long idUser;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phone;
    protected String password;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    protected boolean active;
    protected boolean emailVerified;
    protected boolean phoneVerified;

    

    public CustomUserDetails(User user) {
        this.idUser = user.getIdUser();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.password = user.getPassword();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.active = user.isActive();
        this.emailVerified = user.isEmailVerified();
        this.phoneVerified = user.isPhoneVerified();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // ou retourne des rôles si besoin
    }
    @Override
    public String getUsername() {
        return this.email;
    }

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
        return this.active;
    }
}