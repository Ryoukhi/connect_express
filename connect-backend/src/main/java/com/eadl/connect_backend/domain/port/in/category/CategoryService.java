package com.eadl.connect_backend.domain.port.in.category;

import com.eadl.connect_backend.domain.model.Category;
import java.util.List;
import java.util.Optional;

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
     * Récupère une catégorie par son code/slug
     * @param code Le code unique de la catégorie
     * @return Optional contenant la catégorie si trouvée, sinon Optional vide
     */
    Optional<Category> getCategoryByCode(String code);

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
     * Récupère les catégories parentes (catégories de premier niveau)
     * @return Liste des catégories sans parent
     */
    List<Category> getParentCategories();

    /**
     * Récupère les sous-catégories d'une catégorie parente
     * @param parentId L'identifiant de la catégorie parente
     * @return Liste des sous-catégories
     */
    List<Category> getSubCategories(Long parentId);

    /**
     * Récupère les catégories les plus populaires
     * Basé sur le nombre de services ou réservations associés
     * @param limit Le nombre maximum de catégories à retourner
     * @return Liste des catégories populaires
     */
    List<Category> getPopularCategories(int limit);

    // ========== UPDATE ==========

    /**
     * Met à jour complètement une catégorie
     * @param id L'identifiant de la catégorie à modifier
     * @param category L'objet catégorie avec les nouvelles données
     * @return La catégorie mise à jour
     * @throws EntityNotFoundException si la catégorie n'existe pas
     */
    Category updateCategory(Long id, Category category);

    /**
     * Met à jour le nom et la description d'une catégorie
     * @param id L'identifiant de la catégorie
     * @param name Le nouveau nom
     * @param description La nouvelle description
     * @return La catégorie mise à jour
     */
    Category updateCategoryInfo(Long id, String name, String description);

    /**
     * Met à jour l'image/icône d'une catégorie
     * @param id L'identifiant de la catégorie
     * @param imageUrl L'URL de la nouvelle image
     * @return La catégorie mise à jour
     */
    Category updateCategoryImage(Long id, String imageUrl);

    /**
     * Change l'ordre d'affichage d'une catégorie
     * @param id L'identifiant de la catégorie
     * @param displayOrder Le nouvel ordre d'affichage
     * @return La catégorie mise à jour
     */
    Category updateCategoryOrder(Long id, int displayOrder);

    // ========== DELETE ==========

    /**
     * Supprime définitivement une catégorie de la base de données
     * @param id L'identifiant de la catégorie à supprimer
     * @throws EntityNotFoundException si la catégorie n'existe pas
     * @throws IllegalStateException si la catégorie contient des services
     */
    void deleteCategory(Long id);

    /**
     * Désactive une catégorie sans la supprimer physiquement (soft delete)
     * La catégorie reste dans la base mais est marquée comme inactive
     * @param id L'identifiant de la catégorie à désactiver
     */
    void softDeleteCategory(Long id);

    /**
     * Réactive une catégorie précédemment désactivée
     * @param id L'identifiant de la catégorie à réactiver
     */
    void reactivateCategory(Long id);

    // ========== MÉTHODES UTILITAIRES ==========

    /**
     * Vérifie si un nom de catégorie existe déjà
     * @param name Le nom à vérifier
     * @return true si le nom existe déjà, false sinon
     */
    boolean existsByName(String name);

    /**
     * Vérifie si un code de catégorie existe déjà
     * @param code Le code à vérifier
     * @return true si le code existe déjà, false sinon
     */
    boolean existsByCode(String code);

    /**
     * Compte le nombre total de catégories dans le système
     * @return Le nombre de catégories
     */
    long countCategories();

    /**
     * Compte le nombre de catégories actives
     * @return Le nombre de catégories actives
     */
    long countActiveCategories();

    /**
     * Recherche des catégories par mot-clé
     * La recherche porte sur le nom et la description
     * @param keyword Le mot-clé de recherche
     * @return Liste des catégories correspondant à la recherche
     */
    List<Category> searchCategories(String keyword);

    /**
     * Compte le nombre de services dans une catégorie
     * @param categoryId L'identifiant de la catégorie
     * @return Le nombre de services
     */
    long countServicesByCategory(Long categoryId);
}
