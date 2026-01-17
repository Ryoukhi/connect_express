package com.eadl.connect_backend.application.service.user;

import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.in.user.ProfileService;
import com.eadl.connect_backend.domain.port.out.external.StorageService;
import com.eadl.connect_backend.domain.port.out.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final StorageService storageService;

    @Override
    public User updateProfilePhoto(Long idUser, byte[] photoData, String fileName) {
        log.info("Mise à jour de la photo de profil pour l'utilisateur id: {}", idUser);
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        // Supprimer l'ancienne photo si elle existe
        String oldUrl = user.getProfilePhotoUrl();
        if (oldUrl != null) {
            try {
                storageService.deleteFile(oldUrl);
            } catch (Exception e) {
                log.warn("Impossible de supprimer l'ancienne photo de profil: {}", oldUrl, e);
            }
        }

        String folder = "profile_photos/" + idUser;
        String url = storageService.uploadFile(photoData, fileName, folder, "image/jpeg"); // Default to jpeg

        user.updateProfilePhoto(url);
        return userRepository.save(user);
    }

    @Override
    public User deleteProfilePhoto(Long idUser) {
        log.info("Suppression de la photo de profil pour l'utilisateur id: {}", idUser);
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        String url = user.getProfilePhotoUrl();
        if (url != null) {
            try {
                storageService.deleteFile(url);
            } catch (Exception e) {
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
                storageService.deleteFile(url);
            } catch (Exception e) {
                log.warn("Impossible de supprimer le fichier de photo de profil: {}", url, e);
            }
        }

        userRepository.delete(user);
        log.info("Compte supprimé pour l'utilisateur id: {}", idUser);
    }

}