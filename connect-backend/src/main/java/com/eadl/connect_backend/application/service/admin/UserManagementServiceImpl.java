package com.eadl.connect_backend.application.service.admin;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eadl.connect_backend.domain.model.admin.ActionType;
import com.eadl.connect_backend.domain.model.admin.AdminAction;
import com.eadl.connect_backend.domain.model.notification.NotificationType;
import com.eadl.connect_backend.domain.model.user.Role;
import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.in.admin.UserManagementService;
import com.eadl.connect_backend.domain.port.in.notification.NotificationService;
import com.eadl.connect_backend.domain.port.out.persistence.AdminActionRepository;
import com.eadl.connect_backend.domain.port.out.persistence.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class UserManagementServiceImpl implements UserManagementService {
    
    private final UserRepository userRepository;
    private final AdminActionRepository adminActionRepository;
    private final NotificationService notificationService;
    
    public UserManagementServiceImpl(UserRepository userRepository,
                                    AdminActionRepository adminActionRepository,
                                    NotificationService notificationService) {
        this.userRepository = userRepository;
        this.adminActionRepository = adminActionRepository;
        this.notificationService = notificationService;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers(int page, int size) {
        return userRepository.findAll(page, size);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> searchUsers(String searchTerm, String role) {
        if (role != null && !role.isEmpty()) {
            Role userRole = Role.valueOf(role.toUpperCase());
            return userRepository.findByRole(userRole).stream()
                .filter(u -> matchesSearchTerm(u, searchTerm))
                .toList();
        }
        
        return userRepository.searchByNameOrEmail(searchTerm);
    }
    
    @Override
    public User suspendUser(Long idAdmin, Long idUser, String reason) {
        User user = userRepository.findById(idUser)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        
        // Suspendre l'utilisateur
        user.deactivate();
        user = userRepository.save(user);
        
        // Logger l'action admin
        AdminAction action = AdminAction.createWithTarget(
            idAdmin, idUser, ActionType.USER_SUSPENDED, reason
        );
        adminActionRepository.save(action);
        
        // Notifier l'utilisateur
        notificationService.sendNotification(
            idUser,
            NotificationType.ACCOUNT_SUSPENDED,
            "Compte suspendu",
            "Votre compte a été suspendu. Raison: " + reason
        );
        
        return user;
    }
    
    @Override
    public User reactivateUser(Long idAdmin, Long idUser, String reason) {
        User user = userRepository.findById(idUser)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        
        // Réactiver l'utilisateur
        user.activate();
        user = userRepository.save(user);
        
        // Logger l'action admin
        AdminAction action = AdminAction.createWithTarget(
            idAdmin, idUser, ActionType.USER_REACTIVATED, reason
        );
        adminActionRepository.save(action);
        
        // Notifier l'utilisateur
        notificationService.sendNotification(
            idUser,
            NotificationType.ACCOUNT_REACTIVATED,
            "Compte réactivé",
            "Votre compte a été réactivé"
        );
        
        return user;
    }
    
    @Override
    public void deleteUser(Long idAdmin, Long idUser, String reason) {
        User user = userRepository.findById(idUser)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        
        // Logger l'action admin AVANT de supprimer
        AdminAction action = AdminAction.createWithTarget(
            idAdmin, idUser, ActionType.USER_DELETED, reason
        );
        adminActionRepository.save(action);
        
        // Supprimer l'utilisateur
        userRepository.delete(user);
    }
    
    @Override
    public User changeUserRole(Long idAdmin, Long idUser, String newRole, String reason) {
        User user = userRepository.findById(idUser)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        
        String oldRole = user.getRole().name();
        
        // Logger l'action admin
        AdminAction action = AdminAction.createWithTarget(
            idAdmin, idUser, ActionType.USER_ROLE_CHANGED,
            "Changement de " + oldRole + " vers " + newRole + ". Raison: " + reason
        );
        adminActionRepository.save(action);
        
        // Note: Le changement de rôle nécessiterait une refonte
        // Car Client, Technician, Admin sont des classes différentes
        // Pour l'instant, on log juste l'action
        
        return user;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getSuspendedUsers() {
        return userRepository.findByActive(false);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> getRecentUsers(int days) {
        LocalDateTime sinceDate = LocalDateTime.now().minusDays(days);
        return userRepository.findCreatedAfter(sinceDate);
    }
    
    private boolean matchesSearchTerm(User user, String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return true;
        }
        
        String term = searchTerm.toLowerCase();
        return user.getFirstName().toLowerCase().contains(term) ||
               user.getLastName().toLowerCase().contains(term) ||
               user.getEmail().toLowerCase().contains(term);
    }
}
