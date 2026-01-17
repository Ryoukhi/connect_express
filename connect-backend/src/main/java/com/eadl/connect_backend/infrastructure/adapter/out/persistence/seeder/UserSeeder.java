package com.eadl.connect_backend.infrastructure.adapter.out.persistence.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import com.eadl.connect_backend.domain.model.user.Role;
import com.eadl.connect_backend.domain.port.out.security.PasswordEncoder;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa.UserJpaRepository;
import java.time.LocalDateTime;

@Component
@Profile("dev") // ne s'exécute que si le profil dev est actif
public class UserSeeder implements CommandLineRunner {

    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(UserJpaRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Si la table contient déjà des utilisateurs, on ne fait rien
        if (userRepository.count() > 0) {
            System.out.println("⚠️ Users already exist, skipping seeding.");
            return;
        }

        System.out.println("🌱 Seeding users for development...");

        // 3 ADMINS
        for (int i = 1; i <= 3; i++) {
            UserEntity admin = buildUser(
                    "Admin" + i,
                    "System",
                    "admin" + i + "@mail.com",
                    Role.ADMIN
            );
            userRepository.save(admin);
            System.out.println("✅ Admin created: " + admin.getEmail());
        }

        // 5 TECHNICIENS
        for (int i = 1; i <= 5; i++) {
            UserEntity tech = buildUser(
                    "Tech" + i,
                    "Worker",
                    "tech" + i + "@mail.com",
                    Role.TECHNICIAN
            );
            userRepository.save(tech);
            System.out.println("✅ Technician created: " + tech.getEmail());
        }

        // 10 CLIENTS
        for (int i = 1; i <= 10; i++) {
            UserEntity client = buildUser(
                    "Client" + i,
                    "User",
                    "client" + i + "@mail.com",
                    Role.CLIENT
            );
            userRepository.save(client);
            System.out.println("✅ Client created: " + client.getEmail());
        }

        System.out.println("🎉 All users seeded successfully!");
    }

    private UserEntity buildUser(String firstName, String lastName, String email, Role role) {
        UserEntity user = new UserEntity();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("Password@123")); // mot de passe par défaut
        user.setPhone("690000000"); // numéro générique
        user.setCity("Douala"); // valeur par défaut
        user.setNeighborhood("Akwa"); // valeur par défaut
        user.setProfilePhotoUrl(null);
        user.setRole(role);
        user.setActive(true);
        user.setEmailVerified(false);
        user.setPhoneVerified(false);
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        return user;
    }
}