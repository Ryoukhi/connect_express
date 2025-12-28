package com.eadl.connect_backend.domain.port.in.user;

import java.util.Optional;

import com.eadl.connect_backend.domain.model.user.User;

/**
 * Port IN - Service Utilisateur
 * Use cases généraux pour les utilisateurs
 */
public interface UserService {

    Optional<User> getById(Long userId);

    Optional<User> getByEmail(String email);

    Optional<User> getByPhone(String phone);

    User updateProfile(User user);

    void changePassword(String currentPassword, String newPassword);

    User activate(Long userId);
    User deactivate(Long userId);

    boolean emailExists(String email);
    
    boolean phoneExists(String phone);
}
