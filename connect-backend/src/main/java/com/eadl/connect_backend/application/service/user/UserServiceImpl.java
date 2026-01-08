package com.eadl.connect_backend.application.service.user;

import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.exception.InvalidPasswordException;
import com.eadl.connect_backend.domain.port.exception.UserNotFoundException;
import com.eadl.connect_backend.domain.port.in.user.UserService;
import com.eadl.connect_backend.domain.port.out.persistence.UserRepository;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CurrentUserProvider currentUserProvider;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            CurrentUserProvider currentUserProvider,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.currentUserProvider = currentUserProvider;
        this.passwordEncoder = passwordEncoder;
    }

    // ===================== READ =====================

    @Override
    public Optional<User> getById(Long userId) {
        log.debug("Recherche utilisateur par id={}", userId);
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        log.debug("Recherche utilisateur par email={}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getByPhone(String phone) {
        log.debug("Recherche utilisateur par téléphone={}", phone);
        return userRepository.findByPhone(phone);
    }

    // ===================== UPDATE =====================

    @Override
    public User updateProfile(User user) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        log.info("Mise à jour du profil utilisateur id={}", currentUserId);

        User existingUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> {
                    log.error("Échec de mise à jour : utilisateur introuvable id={}", currentUserId);
                    return new UserNotFoundException("User not found with ID: " + currentUserId);
                });

        existingUser.updateProfile(
                user.getFirstName(),
                user.getLastName(),
                user.getPhone()
        );

        User savedUser = userRepository.save(existingUser);
        log.info("Profil utilisateur mis à jour avec succès id={}", currentUserId);
        return savedUser;
    }

    @Override
    public void changePassword(String currentPassword, String newPassword) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        log.info("Changement de mot de passe pour utilisateur id={}", currentUserId);

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> {
                    log.error("Échec du changement de mot de passe : utilisateur introuvable id={}", currentUserId);
                    return new UserNotFoundException("User not found with ID: " + currentUserId);
                });

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            log.warn("Mot de passe actuel incorrect pour utilisateur id={}", currentUserId);
            throw new InvalidPasswordException("Mot de passe actuel incorrect");
        }

        user.changePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Mot de passe modifié avec succès pour utilisateur id={}", currentUserId);
    }

    @Override
    public User activate(Long userId) {
        log.info("Activation de l'utilisateur id={}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Échec d'activation : utilisateur introuvable id={}", userId);
                    return new UserNotFoundException("User not found with ID: " + userId);
                });

        user.activate();
        User savedUser = userRepository.save(user);
        log.info("Utilisateur activé id={}", userId);
        return savedUser;
    }

    @Override
    public User deactivate(Long userId) {
        log.info("Désactivation de l'utilisateur id={}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("Échec de désactivation : utilisateur introuvable id={}", userId);
                    return new UserNotFoundException("User not found with ID: " + userId);
                });

        user.deactivate();
        User savedUser = userRepository.save(user);
        log.info("Utilisateur désactivé id={}", userId);
        return savedUser;
    }

    // ===================== CHECK =====================

    @Override
    public boolean emailExists(String email) {
        boolean exists = userRepository.existsByEmail(email);
        log.debug("Vérification existence email={} : {}", email, exists);
        return exists;
    }

    @Override
    public boolean phoneExists(String phone) {
        boolean exists = userRepository.existsByPhone(phone);
        log.debug("Vérification existence téléphone={} : {}", phone, exists);
        return exists;
    }
}
