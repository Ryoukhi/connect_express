package com.eadl.connect_backend.domain.port.in.category;

import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.category.Category;

import jakarta.persistence.EntityNotFoundException;

/**
 * Interface de service pour la gestion des catégories
 * Définit les opérations CRUD et les fonctionnalités métier liées aux catégories
 */
public interface CategoryService {

    // ========== CREATE ==========

    /**
     * Crée une nouvelle catégorie dans le système
     * @param category L'objet catégorie à créer
     * @return La catégorie créée avec son ID généré
     * @throws IllegalArgumentException si le nom de catégorie existe déjà
     */
    Category createCategory(Category category);

    // ========== READ ==========

    /**
     * Récupère une catégorie par son identifiant
     * @param id L'identifiant de la catégorie
     * @return Optional contenant la catégorie si trouvée, sinon Optional vide
     */
    Optional<Category> getCategoryById(Long id);

    /**
     * Récupère une catégorie par son nom
     * @param name Le nom de la catégorie
     * @return Optional contenant la catégorie si trouvée, sinon Optional vide
     */
    Optional<Category> getCategoryByName(String name);


    /**
     * Récupère la liste complète de toutes les catégories
     * @return Liste de toutes les catégories enregistrées
     */
    List<Category> getAllCategories();

    /**
     * Récupère toutes les catégories actives
     * @return Liste des catégories actives
     */
    List<Category> getActiveCategories();

    /**
     * Met à jour l'icône d'une catégorie
     */
    Category updateCategoryIcon(Long idCategory, String iconUrl);
    
    /**
     * Met à jour l'ordre d'affichage
     */
    Category updateDisplayOrder(Long idCategory, Integer order);
    
    /**
     * Active une catégorie
     */
    Category activateCategory(Long idCategory);
    
    /**
     * Désactive une catégorie
     */
    Category deactivateCategory(Long idCategory);

    /**
     * Compte le nombre de techniciens par catégorie
     */
    Long countTechniciansByCategory(Long idCategory);
    

    // ========== UPDATE ==========

    /**
     * Met à jour complètement une catégorie
     * @param id L'identifiant de la catégorie à modifier
     * @param category L'objet catégorie avec les nouvelles données
     * @return La catégorie mise à jour
     * @throws EntityNotFoundException si la catégorie n'existe pas
     */
    Category updateCategory(Long id, Category category);


    // ========== DELETE ==========

    /**
     * Supprime définitivement une catégorie de la base de données
     * @param id L'identifiant de la catégorie à supprimer
     * @throws EntityNotFoundException si la catégorie n'existe pas
     * @throws IllegalStateException si la catégorie contient des services
     */
    void deleteCategory(Long id);

    
}
