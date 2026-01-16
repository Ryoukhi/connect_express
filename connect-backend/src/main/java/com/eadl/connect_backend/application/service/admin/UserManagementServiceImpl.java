package com.eadl.connect_backend.application.service.admin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eadl.connect_backend.domain.model.user.Role;
import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.exception.UserNotFoundException;
import com.eadl.connect_backend.domain.port.in.admin.UserManagementService;
import com.eadl.connect_backend.domain.port.out.persistence.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserManagementServiceImpl implements UserManagementService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers(int page, int size) {
        log.debug("Récupération des utilisateurs page={}, size={}", page, size);
        return userRepository.findAll(page, size);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> searchUsers(String searchTerm, String roleStr) {
        log.debug("Recherche utilisateurs term={}, role={}", searchTerm, roleStr);

        List<User> users;
        if (roleStr != null && !roleStr.isEmpty()) {
            try {
                Role role = Role.valueOf(roleStr.toUpperCase());
                users = userRepository.findByRole(role);
            } catch (IllegalArgumentException e) {
                log.warn("Role invalide: {}", roleStr);
                return List.of();
            }
        } else {
            users = userRepository.findAll();
        }

        if (searchTerm != null && !searchTerm.isBlank()) {
            String term = searchTerm.toLowerCase();
            return users.stream()
                    .filter(u -> (u.getFirstName() != null && u.getFirstName().toLowerCase().contains(term)) ||
                            (u.getLastName() != null && u.getLastName().toLowerCase().contains(term)) ||
                            (u.getEmail() != null && u.getEmail().toLowerCase().contains(term)))
                    .collect(Collectors.toList());
        }

        return users;
    }

    @Override
    public User suspendUser(Long idAdmin, Long idUser, String reason) {
        log.info("Suspension utilisateur id={} par admin id={}, raison={}", idUser, idAdmin, reason);
        User user = getUser(idUser);
        user.deactivate();
        // TODO: Enregistrer l'action dans AdminActionRepository si nécessaire
        return userRepository.save(user);
    }

    @Override
    public User reactivateUser(Long idAdmin, Long idUser, String reason) {
        log.info("Réactivation utilisateur id={} par admin id={}, raison={}", idUser, idAdmin, reason);
        User user = getUser(idUser);
        user.activate();
        // TODO: Enregistrer l'action dans AdminActionRepository
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long idAdmin, Long idUser, String reason) {
        log.info("Suppression utilisateur id={} par admin id={}, raison={}", idUser, idAdmin, reason);
        User user = getUser(idUser);
        userRepository.delete(user);
        // TODO: Enregistrer l'action
    }

    @Override
    public User changeUserRole(Long idAdmin, Long idUser, String newRole, String reason) {
        log.info("Changement rôle utilisateur id={} vers {} par admin id={}, raison={}", idUser, newRole, idAdmin,
                reason);
        User user = getUser(idUser);
        try {
            Role role = Role.valueOf(newRole.toUpperCase());
            user.setRole(role);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rôle invalide: " + newRole);
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getSuspendedUsers() {
        return userRepository.findByActive(false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getRecentUsers(int days) {
        LocalDateTime limit = LocalDateTime.now().minusDays(days);
        // Implémentation in-memory car le repository ne semble pas avoir
        // findByCreatedAtAfter
        // Idéalement à ajouter au repository
        return userRepository.findAll().stream()
                .filter(u -> u.getCreatedAt().isAfter(limit))
                .collect(Collectors.toList());
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'id " + id));
    }
}
