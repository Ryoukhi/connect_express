package com.eadl.connect_backend.application.service.user;

import com.eadl.connect_backend.domain.model.user.Role;
import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.in.user.UserService;
import com.eadl.connect_backend.domain.port.out.persistence.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
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

    @Override
    public Optional<User> getUserById(Long idUser) {
        return userRepository.findById(idUser);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getUserByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    public User updateProfile(Long idUser, String firstName, String lastName, String phone) {
        log.info("Mise à jour du profil utilisateur id={}", idUser);
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));

        // If phone is changed, ensure it's not already taken by another user
        if (phone != null && !phone.equals(user.getPhone()) && userRepository.existsByPhone(phone)) {
            throw new IllegalArgumentException("Ce numéro de téléphone est déjà utilisé");
        }

        user.updateProfile(firstName == null ? user.getFirstName() : firstName,
                lastName == null ? user.getLastName() : lastName,
                phone == null ? user.getPhone() : phone);

        return userRepository.save(user);
    }

    @Override
    public void changePassword(Long idUser, String oldPassword, String newPassword) {
        log.info("Changement de mot de passe pour l'utilisateur id={}", idUser);
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Mot de passe actuel incorrect");
        }

        user.changePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public User verifyEmail(Long idUser, String verificationToken) {
        log.info("Vérification d'email pour l'utilisateur id={}", idUser);
        if (verificationToken == null || verificationToken.trim().isEmpty()) {
            throw new IllegalArgumentException("Token de vérification invalide");
        }

        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));

        // Token validation is out of scope here; accept provided token for now
        user.verifyEmail();
        return userRepository.save(user);
    }

    @Override
    public User verifyPhone(Long idUser, String verificationCode) {
        log.info("Vérification de téléphone pour l'utilisateur id={}", idUser);
        if (verificationCode == null || verificationCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Code de vérification invalide");
        }

        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));

        // Code validation is out of scope here; accept provided code for now
        user.verifyPhone();
        return userRepository.save(user);
    }

    @Override
    public User activateAccount(Long idUser) {
        log.info("Activation du compte utilisateur id={}", idUser);
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));

        user.activate();
        return userRepository.save(user);
    }

    @Override
    public User deactivateAccount(Long idUser) {
        log.info("Désactivation du compte utilisateur id={}", idUser);
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));

        user.deactivate();
        return userRepository.save(user);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean phoneExists(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUsersByRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role must be provided");
        }

        Role r;
        try {
            r = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Role inconnu: " + role);
        }

        return userRepository.findByRole(r);
    } 
}