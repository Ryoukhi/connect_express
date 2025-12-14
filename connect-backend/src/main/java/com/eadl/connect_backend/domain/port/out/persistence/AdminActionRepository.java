package com.eadl.connect_backend.domain.port.out.persistence;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.admin.ActionType;
import com.eadl.connect_backend.domain.model.admin.AdminAction;

/**
 * Port OUT - Repository AdminAction
 */
public interface AdminActionRepository {
    
    /**
     * Sauvegarde une action admin
     */
    AdminAction save(AdminAction action);
    
    /**
     * Récupère une action par son ID
     */
    Optional<AdminAction> findById(Long idAction);
    
    /**
     * Récupère toutes les actions d'un admin
     */
    List<AdminAction> findByAdminId(Long idAdmin);
    
    /**
     * Récupère toutes les actions sur un utilisateur cible
     */
    List<AdminAction> findByTargetUserId(Long idTargetUser);
    
    /**
     * Récupère les actions par type
     */
    List<AdminAction> findByActionType(ActionType actionType);
    
    /**
     * Récupère les actions dans une période
     */
    List<AdminAction> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    /**
     * Récupère les actions récentes
     */
    List<AdminAction> findRecent(int limit);
    
    /**
     * Récupère les actions critiques
     */
    List<AdminAction> findCriticalActions();
    
    /**
     * Récupère toutes les actions avec pagination
     */
    List<AdminAction> findAll(int page, int size);
    
    /**
     * Compte les actions
     */
    Long count();
    
    /**
     * Compte les actions par type
     */
    Long countByActionType(ActionType actionType);
    
    /**
     * Compte les actions d'un admin
     */
    Long countByAdminId(Long idAdmin);
}