package com.eadl.connect_backend.application.service.user;
import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.out.persistence.UserRepository;
import com.eadl.connect_backend.domain.port.in.user.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implémentation du service d'authentification
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User login(String email, String password) {


        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Email ou mot de passe incorrect"));

        if (!user.isActive()) {
            throw new IllegalStateException("Ce compte est désactivé");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {

            throw new IllegalArgumentException("Email ou mot de passe incorrect");
        }

        return user;
    }

    @Override
    public void logout(Long idUser) {
       
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
        // Update the updatedAt timestamp to reflect the logout action
        user.setUpdatedAt(java.time.LocalDateTime.now());
        userRepository.save(user);
        
    }

    @Override
    public User register(User user) {

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

        user.changePassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        return savedUser;
    }


}
