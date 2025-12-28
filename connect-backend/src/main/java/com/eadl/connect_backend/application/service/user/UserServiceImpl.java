package com.eadl.connect_backend.application.service.user;

import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.exception.InvalidPasswordException;
import com.eadl.connect_backend.domain.port.exception.UserNotFoundException;
import com.eadl.connect_backend.domain.port.in.user.UserService;
import com.eadl.connect_backend.domain.port.out.persistence.UserRepository;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
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
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    // ===================== UPDATE =====================

    /**
     * Met à jour le profil de l'utilisateur connecté
     */
    @Override
    public User updateProfile(User user) {
        Long currentUserId = currentUserProvider.getCurrentUserId();

        User existingUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + currentUserId));

        existingUser.updateProfile(
                user.getFirstName(),
                user.getLastName(),
                user.getPhone()
        );

        return userRepository.save(existingUser);
    }

    @Override
    public void changePassword(String currentPassword, String newPassword) {
        Long currentUserId = currentUserProvider.getCurrentUserId();

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + currentUserId));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidPasswordException("Mot de passe actuel incorrect");
        }

        user.changePassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }


    @Override
    public User activate(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        user.activate();

        return userRepository.save(user);
    }

    @Override
    public User deactivate(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        user.deactivate();

        return userRepository.save(user);
    }

    // ===================== CHECK =====================

    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean phoneExists(String phone) {
        return userRepository.existsByPhone(phone);
    }
}