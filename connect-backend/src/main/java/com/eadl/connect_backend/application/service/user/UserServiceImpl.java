package com.eadl.connect_backend.application.service.user;

import com.eadl.connect_backend.domain.model.User;
import com.eadl.connect_backend.domain.port.in.user.UserService;
import com.eadl.connect_backend.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service de gestion des utilisateurs
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ========== CREATE ==========

    @Override
    public User createUser(User user) {
        log.info("Création d'un nouvel utilisateur avec l'email: {}", user.getEmail());

        if (existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà");
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        User savedUser = userRepository.save(user);
        log.info("Utilisateur créé avec succès avec l'ID: {}", savedUser.getId());
        return savedUser;
    }

    // ========== READ ==========

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        log.debug("Recherche de l'utilisateur avec l'ID: {}", id);
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        log.debug("Recherche de l'utilisateur avec l'email: {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        log.debug("Récupération de tous les utilisateurs");
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersByRole(String role) {
        log.debug("Récupération des utilisateurs avec le rôle: {}", role);
        return userRepository.findByRole(role);
    }

    // ========== UPDATE ==========

    @Override
    public User updateUser(Long id, User user) {
        log.info("Mise à jour de l'utilisateur avec l'ID: {}", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + id));

        if (user.getFirstName() != null) {
            existingUser.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            existingUser.setLastName(user.getLastName());
        }
        if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
            if (existsByEmail(user.getEmail())) {
                throw new IllegalArgumentException("Cet email est déjà utilisé");
            }
            existingUser.setEmail(user.getEmail());
        }
        if (user.getPhone() != null) {
            existingUser.setPhone(user.getPhone());
        }
        if (user.getRole() != null) {
            existingUser.setRole(user.getRole());
        }

        User updatedUser = userRepository.save(existingUser);
        log.info("Utilisateur mis à jour avec succès: {}", id);
        return updatedUser;
    }

    @Override
    public User updateUserProfile(Long id, User user) {
        // TODO: Implémenter
        return null;
    }

    @Override
    public User updateUserPassword(Long id, String oldPassword, String newPassword) {
        // TODO: Implémenter
        return null;
    }

    // ========== DELETE ==========

    @Override
    public void deleteUser(Long id) {
        log.info("Suppression de l'utilisateur avec l'ID: {}", id);

        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + id);
        }

        userRepository.deleteById(id);
        log.info("Utilisateur supprimé avec succès: {}", id);
    }

    @Override
    public void softDeleteUser(Long id) {
        // TODO: Implémenter
    }

    // ========== MÉTHODES UTILITAIRES ==========

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUsers() {
        return userRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> searchUsers(String keyword) {
        // TODO: Implémenter
        return List.of();
    }
}