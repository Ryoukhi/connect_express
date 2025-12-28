package com.eadl.connect_backend.application.service.user;
import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.out.persistence.UserRepository;
import com.eadl.connect_backend.domain.port.in.user.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implémentation du service d'authentification
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User login(String email, String password) {
        log.info("Tentative de connexion pour l'email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email ou mot de passe incorrect"));

        if (!user.isActive()) {
            throw new IllegalStateException("Ce compte est désactivé");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("Tentative de connexion échouée pour l'email: {}", email);
            throw new IllegalArgumentException("Email ou mot de passe incorrect");
        }

        log.info("Connexion réussie pour l'utilisateur: {}", email);
        return user;
    }

    @Override
    public void logout(Long idUser) {
        log.info("Déconnexion pour l'utilisateur id: {}", idUser);
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
        // Update the updatedAt timestamp to reflect the logout action
        user.setUpdatedAt(java.time.LocalDateTime.now());
        userRepository.save(user);
        log.info("Déconnexion effectuée pour l'utilisateur id: {}", idUser);
    }

    @Override
    public User register(User user) {
        log.info("Tentative d'inscription pour l'email: {}", user.getEmail());

        if (userRepository.existsByEmail(user.getEmail())) {
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

        // Persist the encoded password using domain method that also updates timestamps
        user.changePassword(passwordEncoder.encode(user.getPassword()));

        // The domain model's concrete subclasses set the Role in their constructors/factories
        // so no need to try to set it here. Ensure active flag is true by default in domain model

        User savedUser = userRepository.save(user);
        log.info("Inscription réussie pour l'email: {}", savedUser.getEmail());

        return savedUser;
    }

    @Override
    public User updateProfile(Long idUser, User updatedUser) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProfile'");
    }

}
