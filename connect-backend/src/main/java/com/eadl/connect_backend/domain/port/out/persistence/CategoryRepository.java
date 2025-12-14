package com.eadl.connect_backend.domain.port.out.persistence;

import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.category.Category;

/**
 * Port OUT - Repository Category
 */
public interface CategoryRepository {
    
    /**
     * Sauvegarde une catégorie
     */
    Category save(Category category);
    
    /**
     * Récupère une catégorie par son ID
     */
    Optional<Category> findById(Long idCategory);
    
    /**
     * Récupère une catégorie par son nom
     */
    Optional<Category> findByName(String name);
    
    /**
     * Récupère toutes les catégories
     */
    List<Category> findAll();
    
    /**
     * Récupère les catégories actives
     */
    List<Category> findByActive(boolean active);
    
    /**
     * Récupère les catégories ordonnées
     */
    List<Category> findAllOrderedByDisplayOrder();
    
    /**
     * Vérifie si une catégorie existe
     */
    boolean existsByName(String name);
    
    /**
     * Compte les catégories
     */
    Long count();
    
    /**
     * Compte les catégories actives
     */
    Long countByActive(boolean active);
    
    /**
     * Supprime une catégorie
     */
    void delete(Category category);
}
