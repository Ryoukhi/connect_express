package com.eadl.connect_backend.domain.port.in.review;

import com.eadl.connect_backend.domain.model.Review;
import java.util.List;
import java.util.Optional;

/**
 * Interface de service pour la gestion des avis et évaluations
 * Définit les opérations CRUD et les fonctionnalités liées aux reviews
 */
public interface ReviewService {

    // ========== CREATE ==========

    /**
     * Crée un nouvel avis
     * @param review L'objet review à créer
     * @return L'avis créé avec son ID généré
     * @throws IllegalArgumentException si l'utilisateur a déjà laissé un avis pour ce service
     */
    Review createReview(Review review);

    /**
     * Crée un avis complet avec note et commentaire
     * @param userId L'identifiant de l'utilisateur
     * @param serviceId L'identifiant du service évalué
     * @param rating La note (généralement de 1 à 5)
     * @param comment Le commentaire textuel
     * @return L'avis créé
     */
    Review createReview(Long userId, Long serviceId, int rating, String comment);

    /**
     * Crée un avis avec images
     * @param userId L'identifiant de l'utilisateur
     * @param serviceId L'identifiant du service
     * @param rating La note
     * @param comment Le commentaire
     * @param imageUrls Liste des URLs des images jointes
     * @return L'avis créé avec les images
     */
    Review createReviewWithImages(Long userId, Long serviceId, int rating, String comment, List<String> imageUrls);

    // ========== READ ==========

    /**
     * Récupère un avis par son identifiant
     * @param id L'identifiant de l'avis
     * @return Optional contenant l'avis si trouvé, sinon Optional vide
     */
    Optional<Review> getReviewById(Long id);

    /**
     * Récupère tous les avis d'un service
     * @param serviceId L'identifiant du service
     * @return Liste des avis du service, triés par date (plus récents en premier)
     */
    List<Review> getServiceReviews(Long serviceId);

    /**
     * Récupère tous les avis laissés par un utilisateur
     * @param userId L'identifiant de l'utilisateur
     * @return Liste des avis de l'utilisateur
     */
    List<Review> getUserReviews(Long userId);

    /**
     * Récupère les avis d'un service avec pagination
     * @param serviceId L'identifiant du service
     * @param page Le numéro de page (commence à 0)
     * @param size Le nombre d'avis par page
     * @return Liste des avis paginés
     */
    List<Review> getServiceReviews(Long serviceId, int page, int size);

    /**
     * Récupère les avis d'un service filtrés par note
     * @param serviceId L'identifiant du service
     * @param rating La note à filtrer (ex: 5 pour les avis 5 étoiles)
     * @return Liste des avis avec la note spécifiée
     */
    List<Review> getReviewsByRating(Long serviceId, int rating);

    /**
     * Récupère les meilleurs avis d'un service (notes les plus élevées)
     * @param serviceId L'identifiant du service
     * @param limit Le nombre maximum d'avis à retourner
     * @return Liste des meilleurs avis
     */
    List<Review> getTopReviews(Long serviceId, int limit);

    /**
     * Récupère les avis récents d'un service (derniers 30 jours)
     * @param serviceId L'identifiant du service
     * @return Liste des avis récents
     */
    List<Review> getRecentReviews(Long serviceId);

    /**
     * Récupère les avis vérifiés d'un service
     * (avis d'utilisateurs ayant réellement utilisé le service)
     * @param serviceId L'identifiant du service
     * @return Liste des avis vérifiés
     */
    List<Review> getVerifiedReviews(Long serviceId);

    /**
     * Vérifie si un utilisateur a déjà laissé un avis pour un service
     * @param userId L'identifiant de l'utilisateur
     * @param serviceId L'identifiant du service
     * @return Optional contenant l'avis si existant
     */
    Optional<Review> getUserReviewForService(Long userId, Long serviceId);

    // ========== UPDATE ==========

    /**
     * Met à jour un avis
     * @param id L'identifiant de l'avis à modifier
     * @param review L'objet review avec les nouvelles données
     * @return L'avis mis à jour
     * @throws UnauthorizedException si l'utilisateur n'est pas l'auteur
     */
    Review updateReview(Long id, Review review);

    /**
     * Met à jour la note et le commentaire d'un avis
     * @param reviewId L'identifiant de l'avis
     * @param userId L'identifiant de l'utilisateur (pour vérification)
     * @param rating La nouvelle note
     * @param comment Le nouveau commentaire
     * @return L'avis mis à jour
     */
    Review updateReviewContent(Long reviewId, Long userId, int rating, String comment);

    /**
     * Marque un avis comme vérifié
     * (utilisateur confirmé comme ayant utilisé le service)
     * @param reviewId L'identifiant de l'avis
     */
    void markAsVerified(Long reviewId);

    /**
     * Ajoute une réponse du prestataire à un avis
     * @param reviewId L'identifiant de l'avis
     * @param providerId L'identifiant du prestataire
     * @param response La réponse textuelle
     * @return L'avis avec la réponse ajoutée
     */
    Review addProviderResponse(Long reviewId, Long providerId, String response);

    /**
     * Ajoute des "likes" à un avis (vote utile)
     * @param reviewId L'identifiant de l'avis
     * @param userId L'identifiant de l'utilisateur qui like
     */
    void likeReview(Long reviewId, Long userId);

    /**
     * Retire le like d'un avis
     * @param reviewId L'identifiant de l'avis
     * @param userId L'identifiant de l'utilisateur
     */
    void unlikeReview(Long reviewId, Long userId);

    // ========== DELETE ==========

    /**
     * Supprime définitivement un avis
     * @param id L'identifiant de l'avis à supprimer
     * @param userId L'identifiant de l'utilisateur (pour vérification)
     * @throws UnauthorizedException si l'utilisateur n'est pas l'auteur
     */
    void deleteReview(Long id, Long userId);

    /**
     * Signale un avis comme inapproprié
     * @param reviewId L'identifiant de l'avis
     * @param userId L'identifiant de l'utilisateur qui signale
     * @param reason La raison du signalement
     */
    void reportReview(Long reviewId, Long userId, String reason);

    /**
     * Archive un avis (soft delete)
     * L'avis reste en base mais n'est plus visible publiquement
     * @param reviewId L'identifiant de l'avis
     */
    void archiveReview(Long reviewId);

    /**
     * Restaure un avis archivé
     * @param reviewId L'identifiant de l'avis
     */
    void restoreReview(Long reviewId);

    // ========== MÉTHODES UTILITAIRES ET STATISTIQUES ==========

    /**
     * Calcule la note moyenne d'un service
     * @param serviceId L'identifiant du service
     * @return La note moyenne (ex: 4.5)
     */
    double calculateAverageRating(Long serviceId);

    /**
     * Compte le nombre total d'avis pour un service
     * @param serviceId L'identifiant du service
     * @return Le nombre d'avis
     */
    long countServiceReviews(Long serviceId);

    /**
     * Compte les avis par note pour un service
     * @param serviceId L'identifiant du service
     * @return Map avec la répartition des notes (ex: {5: 50, 4: 30, 3: 15, 2: 3, 1: 2})
     */
    Object getRatingDistribution(Long serviceId);

    /**
     * Récupère les statistiques complètes des avis d'un service
     * @param serviceId L'identifiant du service
     * @return Objet contenant moyenne, total, répartition, tendance, etc.
     */
    Object getReviewStatistics(Long serviceId);

    /**
     * Vérifie si un utilisateur a déjà laissé un avis pour un service
     * @param userId L'identifiant de l'utilisateur
     * @param serviceId L'identifiant du service
     * @return true si un avis existe, false sinon
     */
    boolean hasUserReviewedService(Long userId, Long serviceId);

    /**
     * Vérifie si un utilisateur peut laisser un avis
     * (généralement après avoir utilisé le service)
     * @param userId L'identifiant de l'utilisateur
     * @param serviceId L'identifiant du service
     * @return true si l'utilisateur peut laisser un avis, false sinon
     */
    boolean canUserReview(Long userId, Long serviceId);

    /**
     * Recherche des avis par mot-clé dans les commentaires
     * @param serviceId L'identifiant du service
     * @param keyword Le mot-clé de recherche
     * @return Liste des avis correspondants
     */
    List<Review> searchReviews(Long serviceId, String keyword);
}
