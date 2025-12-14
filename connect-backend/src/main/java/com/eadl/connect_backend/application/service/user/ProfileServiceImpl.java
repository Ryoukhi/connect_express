package com.eadl.connect_backend.application.service.user;

import com.eadl.connect_backend.domain.model.User;
import com.eadl.connect_backend.domain.port.in.user.ProfileService;
import com.eadl.connect_backend.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implémentation du service de gestion des profils utilisateurs
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;

    // ========== PROFIL UTILISATEUR ==========

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserProfile(Long userId) {
        log.debug("Récupération du profil de l'utilisateur: {}", userId);
        return userRepository.findById(userId);
    }

    @Override
    public User updateUserProfile(Long userId, String bio, String location, String birthDate) {
        log.info("Mise à jour du profil de l'utilisateur: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + userId));

        if (bio != null) {
            user.setBio(bio);
        }
        if (location != null) {
            user.setLocation(location);
        }
        if (birthDate != null) {
            user.setBirthDate(birthDate);
        }

        User updatedUser = userRepository.save(user);
        log.info("Profil mis à jour avec succès pour l'utilisateur: {}", userId);
        return updatedUser;
    }

    @Override
    public User updateProfileVisibility(Long userId, Boolean isPublic) {
        log.info("Mise à jour de la visibilité du profil pour l'utilisateur: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + userId));

        user.setProfilePublic(isPublic);

        User updatedUser = userRepository.save(user);
        log.info("Visibilité du profil mise à jour pour l'utilisateur: {}", userId);
        return updatedUser;
    }

    // ========== PHOTO DE PROFIL ==========

    @Override
    public User updateProfilePicture(Long userId, String profilePictureUrl) {
        log.info("Mise à jour de la photo de profil pour l'utilisateur: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + userId));

        user.setProfilePicture(profilePictureUrl);

        User updatedUser = userRepository.save(user);
        log.info("Photo de profil mise à jour pour l'utilisateur: {}", userId);
        return updatedUser;
    }

    @Override
    public void deleteProfilePicture(Long userId) {
        log.info("Suppression de la photo de profil pour l'utilisateur: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + userId));

        user.setProfilePicture(null);
        userRepository.save(user);
        log.info("Photo de profil supprimée pour l'utilisateur: {}", userId);
    }

    @Override
    public User updateCoverPicture(Long userId, String coverPictureUrl) {
        log.info("Mise à jour de la photo de couverture pour l'utilisateur: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + userId));

        user.setCoverPicture(coverPictureUrl);

        User updatedUser = userRepository.save(user);
        log.info("Photo de couverture mise à jour pour l'utilisateur: {}", userId);
        return updatedUser;
    }

    // ========== PRÉFÉRENCES ==========

    @Override
    public void toggleEmailNotifications(Long userId, Boolean enabled) {
        log.info("Modification des notifications email pour l'utilisateur {}: {}", userId, enabled);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + userId));

        user.setEmailNotifications(enabled);
        userRepository.save(user);
        log.info("Notifications email {} pour l'utilisateur: {}", enabled ? "activées" : "désactivées", userId);
    }

    @Override
    public void togglePushNotifications(Long userId, Boolean enabled) {
        log.info("Modification des notifications push pour l'utilisateur {}: {}", userId, enabled);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + userId));

        user.setPushNotifications(enabled);
        userRepository.save(user);
        log.info("Notifications push {} pour l'utilisateur: {}", enabled ? "activées" : "désactivées", userId);
    }

    @Override
    public void updateLanguage(Long userId, String language) {
        log.info("Mise à jour de la langue pour l'utilisateur {}: {}", userId, language);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + userId));

        user.setLanguage(language);
        userRepository.save(user);
        log.info("Langue mise à jour pour l'utilisateur: {}", userId);
    }

    @Override
    public void updateTheme(Long userId, String theme) {
        log.info("Mise à jour du thème pour l'utilisateur {}: {}", userId, theme);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + userId));

        user.setTheme(theme);
        userRepository.save(user);
        log.info("Thème mis à jour pour l'utilisateur: {}", userId);
    }

    // ========== SÉCURITÉ ET CONFIDENTIALITÉ ==========

    @Override
    public void toggle2FA(Long userId, Boolean enabled) {
        log.info("Modification de l'authentification 2FA pour l'utilisateur {}: {}", userId, enabled);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + userId));

        user.setTwoFactorEnabled(enabled);
        userRepository.save(user);
        log.info("Authentification 2FA {} pour l'utilisateur: {}", enabled ? "activée" : "désactivée", userId);
    }

    @Override
    public void updatePrivacySettings(Long userId, Boolean showEmail, Boolean showPhone, Boolean showActivity) {
        log.info("Mise à jour des paramètres de confidentialité pour l'utilisateur: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + userId));

        if (showEmail != null) {
            user.setShowEmail(showEmail);
        }
        if (showPhone != null) {
            user.setShowPhone(showPhone);
        }
        if (showActivity != null) {
            user.setShowActivity(showActivity);
        }

        userRepository.save(user);
        log.info("Paramètres de confidentialité mis à jour pour l'utilisateur: {}", userId);
    }

    @Override
    public void deactivateAccount(Long userId) {
        log.info("Désactivation du compte pour l'utilisateur: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + userId));

        user.setActive(false);
        userRepository.save(user);
        log.info("Compte désactivé pour l'utilisateur: {}", userId);
    }

    @Override
    public void reactivateAccount(Long userId) {
        log.info("Réactivation du compte pour l'utilisateur: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + userId));

        user.setActive(true);
        userRepository.save(user);
        log.info("Compte réactivé pour l'utilisateur: {}", userId);
    }

    @Override
    public void requestAccountDeletion(Long userId) {
        log.info("Demande de suppression du compte pour l'utilisateur: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + userId));

        user.setDeletionRequested(true);
        userRepository.save(user);
        log.info("Demande de suppression enregistrée pour l'utilisateur: {}", userId);
    }

    // ========== STATISTIQUES ==========

    @Override
    @Transactional(readOnly = true)
    public Object getProfileStatistics(Long userId) {
        log.debug("Récupération des statistiques du profil pour l'utilisateur: {}", userId);

        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + userId);
        }

        // TODO: Récupérer les statistiques réelles depuis d'autres services
        return new Object() {
            public final Long userId = userId;
            public final long totalReservations = 0L;
            public final long totalReviews = 0L;
            public final double averageRating = 0.0;
        };
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isProfileComplete(Long userId) {
        log.debug("Vérification de la complétion du profil pour l'utilisateur: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + userId));

        return user.getFirstName() != null && !user.getFirstName().isEmpty()
                && user.getLastName() != null && !user.getLastName().isEmpty()
                && user.getEmail() != null && !user.getEmail().isEmpty()
                && user.getPhone() != null && !user.getPhone().isEmpty()
                && user.getProfilePicture() != null;
    }

    @Override
    @Transactional(readOnly = true)
    public int getProfileCompletionPercentage(Long userId) {
        log.debug("Calcul du pourcentage de complétion du profil pour l'utilisateur: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID: " + userId));

        int total = 0;
        int completed = 0;

        // Champs obligatoires
        total++; if (user.getFirstName() != null && !user.getFirstName().isEmpty()) completed++;
        total++; if (user.getLastName() != null && !user.getLastName().isEmpty()) completed++;
        total++; if (user.getEmail() != null && !user.getEmail().isEmpty()) completed++;

        // Champs optionnels
        total++; if (user.getPhone() != null && !user.getPhone().isEmpty()) completed++;
        total++; if (user.getProfilePicture() != null) completed++;
        total++; if (user.getBio() != null && !user.getBio().isEmpty()) completed++;
        total++; if (user.getLocation() != null && !user.getLocation().isEmpty()) completed++;
        total++; if (user.getBirthDate() != null) completed++;

        return (int) ((completed * 100.0) / total);
    }
}