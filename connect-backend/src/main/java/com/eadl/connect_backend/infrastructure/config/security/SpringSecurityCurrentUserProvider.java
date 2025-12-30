package com.eadl.connect_backend.infrastructure.config.security;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.out.persistence.UserRepository;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;

@Component
public class SpringSecurityCurrentUserProvider implements CurrentUserProvider {

    private final UserRepository userRepository;

    public SpringSecurityCurrentUserProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public Long getCurrentUserId() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Aucun utilisateur authentifié");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getIdUser();
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user");
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }

    @Override
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}