package com.eadl.connect_backend.application.service.user;

import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.in.user.ProfileService;
import com.eadl.connect_backend.domain.port.out.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Implémentation du service de gestion des profils utilisateurs
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User updateProfilePhoto(Long idUser, byte[] photoData, String fileName) {
        log.info("Mise à jour de la photo de profil pour l'utilisateur id: {}", idUser);
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        try {
            Path dir = Paths.get("uploads", "profile_photos", String.valueOf(idUser));
            Files.createDirectories(dir);
            Path filePath = dir.resolve(fileName);
            Files.write(filePath, photoData);
            String url = filePath.toAbsolutePath().toString();
            user.updateProfilePhoto(url);
            return userRepository.save(user);
        } catch (IOException e) {
            log.error("Erreur lors de l'enregistrement de la photo de profil pour l'utilisateur {}", idUser, e);
            throw new RuntimeException("Erreur lors de l'enregistrement de la photo de profil", e);
        }
    }

    @Override
    public User deleteProfilePhoto(Long idUser) {
        log.info("Suppression de la photo de profil pour l'utilisateur id: {}", idUser);
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        String url = user.getProfilePhotoUrl();
        if (url != null) {
            try {
                Files.deleteIfExists(Paths.get(url));
            } catch (IOException e) {
                log.warn("Impossible de supprimer le fichier de photo de profil: {}", url, e);
            }
            user.deleteProfilePhoto();
            userRepository.save(user);
        }

        return user;
    }

    @Override
    public void deleteAccount(Long idUser, String password) {
        log.info("Suppression du compte pour l'utilisateur id: {}", idUser);
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Mot de passe incorrect");
        }

        // Supprimer la photo de profil associée
        String url = user.getProfilePhotoUrl();
        if (url != null) {
            try {
                Files.deleteIfExists(Paths.get(url));
            } catch (IOException e) {
                log.warn("Impossible de supprimer le fichier de photo de profil: {}", url, e);
            }
        }

        userRepository.delete(user);
        log.info("Compte supprimé pour l'utilisateur id: {}", idUser);
    }

}