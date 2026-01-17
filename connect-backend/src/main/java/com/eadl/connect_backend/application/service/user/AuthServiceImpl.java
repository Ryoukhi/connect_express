package com.eadl.connect_backend.application.service.user;

import com.eadl.connect_backend.application.dto.AuthRequest;
import com.eadl.connect_backend.application.dto.AuthResponse;
import com.eadl.connect_backend.application.dto.RegisterResponseDto;
import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.out.persistence.UserRepository;
import com.eadl.connect_backend.infrastructure.config.security.CustomUserDetails;
import com.eadl.connect_backend.infrastructure.config.security.JwtUtil;
import com.eadl.connect_backend.domain.port.in.user.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implémentation du service d'authentification
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtService;

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        log.info("Tentative de login pour email={}", authRequest.getEmail());

        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> {
                    log.error("Login échoué : utilisateur introuvable email={}", authRequest.getEmail());
                    return new IllegalArgumentException("Email ou mot de passe incorrect");
                });

        if (!user.isActive()) {
            log.warn("Login échoué : compte désactivé id={}", user.getIdUser());
            throw new IllegalStateException("Ce compte est désactivé");
        }

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            log.error("Login échoué : mot de passe incorrect id={}", user.getIdUser());
            throw new IllegalArgumentException("Email ou mot de passe incorrect");
        }

        CustomUserDetails userDetails = new CustomUserDetails(user);
        String token = jwtService.generateToken(userDetails);

        log.info("Login réussi pour l'utilisateur id={}", user.getIdUser());
        log.debug("Token généré: {}", token.substring(0, Math.min(token.length(), 20)) + "...");

        return new AuthResponse(
                token,
                user.getIdUser(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.isActive(),
                user.getProfilePhotoUrl());
    }

    @Override
    public void logout(Long idUser) {
        log.info("Déconnexion utilisateur id={}", idUser);
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> {
                    log.error("Déconnexion échouée : utilisateur introuvable id={}", idUser);
                    return new IllegalArgumentException("Utilisateur introuvable");
                });

        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        log.debug("Utilisateur id={} déconnecté, timestamp mis à jour", idUser);
    }

    @Override
    public RegisterResponseDto register(User user) {
        log.info("Inscription d'un nouvel utilisateur email={}", user.getEmail());

        if (userRepository.existsByEmail(user.getEmail())) {
            log.error("Inscription échouée : email déjà utilisé email={}", user.getEmail());
            throw new IllegalArgumentException("Un compte avec cet email existe déjà");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire");
        }
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("Le prénom est obligatoire");
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }
        if (user.getCity() == null || user.getCity().isEmpty()) {
            throw new IllegalArgumentException("La ville est obligatoire");
        }
        if (user.getNeighborhood() == null || user.getNeighborhood().isEmpty()) {
            throw new IllegalArgumentException("Le quartier est obligatoire");
        }

        user.changePassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        CustomUserDetails userDetails = new CustomUserDetails(savedUser);
        String token = jwtService.generateToken(userDetails);

        log.info("Inscription réussie pour utilisateur id={}, email={}", savedUser.getIdUser(), savedUser.getEmail());
        log.debug("Token généré: {}", token.substring(0, Math.min(token.length(), 20)) + "...");

        return new RegisterResponseDto(
                token,
                savedUser.getIdUser(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getCity(),
                savedUser.getNeighborhood(),
                savedUser.getProfilePhotoUrl());
    }
}
