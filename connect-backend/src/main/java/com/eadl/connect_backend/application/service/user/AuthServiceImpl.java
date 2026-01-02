package com.eadl.connect_backend.application.service.user;
import com.eadl.connect_backend.application.dto.AuthRequest;
import com.eadl.connect_backend.application.dto.AuthResponse;
import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.out.persistence.UserRepository;
import com.eadl.connect_backend.infrastructure.config.security.CustomUserDetails;
import com.eadl.connect_backend.infrastructure.config.security.JwtUtil;
import com.eadl.connect_backend.domain.port.in.user.AuthService;
import lombok.RequiredArgsConstructor;

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
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtService;

    @Override
    public AuthResponse login(AuthRequest authRequest) {


        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email ou mot de passe incorrect"));

        if (!user.isActive()) {
            throw new IllegalStateException("Ce compte est désactivé");
        }

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {

            throw new IllegalArgumentException("Email ou mot de passe incorrect");
        }

        CustomUserDetails userDetails = new CustomUserDetails(user);

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(
                token,
                user.getIdUser(),
                user.getFirstName(),
                user.getLastName(),
                user.isActive()
        );
    }

    @Override
    public void logout(Long idUser) {
       
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
        // Update the updatedAt timestamp to reflect the logout action
        user.setUpdatedAt(LocalDateTime.now());
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

        if (user.getCity() == null || user.getCity().isEmpty()) {
            throw new IllegalArgumentException("La ville est obligatoire");
        }

        if (user.getNeighborhood() == null || user.getNeighborhood().isEmpty()) {
            throw new IllegalArgumentException("Le quartier est obligatoire");
        }

        user.changePassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }
}
