package com.eadl.connect_backend.application.service.category;

import com.eadl.connect_backend.domain.model.Category;
import com.eadl.connect_backend.domain.port.in.category.CategoryService;
import com.eadl.connect_backend.domain.port.out.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service de gestion des catégories
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    // ========== CREATE ==========

    @Override
    public Category createCategory(Category category) {
        log.info("Création d'une nouvelle catégorie: {}", category.getName());

        if (category.getName() != null && existsByName(category.getName())) {
            throw new IllegalArgumentException("Une catégorie avec ce nom existe déjà");
        }

        if (category.getCode() != null && existsByCode(category.getCode())) {
            throw new IllegalArgumentException("Une catégorie avec ce code existe déjà");
        }

        if (category.getActive() == null) {
            category.setActive(true);
        }

        Category savedCategory = categoryRepository.save(category);
        log.info("Catégorie créée avec succès avec l'ID: {}", savedCategory.getId());
        return savedCategory;
    }

    // ========== READ ==========

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> getCategoryById(Long id) {
        log.debug("Recherche de la catégorie avec l'ID: {}", id);
        return categoryRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> getCategoryByName(String name) {
        log.debug("Recherche de la catégorie avec le nom: {}", name);
        return categoryRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> getCategoryByCode(String code) {
        log.debug("Recherche de la catégorie avec le code: {}", code);
        return categoryRepository.findByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        log.debug("Récupération de toutes les catégories");
        return categoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getActiveCategories() {
        // TODO: Implémenter
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getParentCategories() {
        // TODO: Implémenter
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getSubCategories(Long parentId) {
        // TODO: Implémenter
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> getPopularCategories(int limit) {
        // TODO: Implémenter
        return List.of();
    }

    // ========== UPDATE ==========

    @Override
    public Category updateCategory(Long id, Category category) {
        log.info("Mise à jour de la catégorie avec l'ID: {}", id);

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Catégorie non trouvée avec l'ID: " + id));

        if (category.getName() != null && !category.getName().equals(existingCategory.getName())) {
            if (existsByName(category.getName())) {
                throw new IllegalArgumentException("Ce nom de catégorie est déjà utilisé");
            }
            existingCategory.setName(category.getName());
        }

        if (category.getCode() != null && !category.getCode().equals(existingCategory.getCode())) {
            if (existsByCode(category.getCode())) {
                throw new IllegalArgumentException("Ce code de catégorie est déjà utilisé");
            }
            existingCategory.setCode(category.getCode());
        }

        if (category.getDescription() != null) {
            existingCategory.setDescription(category.getDescription());
        }

        if (category.getImageUrl() != null) {
            existingCategory.setImageUrl(category.getImageUrl());
        }

        if (category.getDisplayOrder() != null) {
            existingCategory.setDisplayOrder(category.getDisplayOrder());
        }

        if (category.getParentId() != null) {
            existingCategory.setParentId(category.getParentId());
        }

        Category updatedCategory = categoryRepository.save(existingCategory);
        log.info("Catégorie mise à jour avec succès: {}", id);
        return updatedCategory;
    }

    @Override
    public Category updateCategoryInfo(Long id, String name, String description) {
        // TODO: Implémenter
        return null;
    }

    @Override
    public Category updateCategoryImage(Long id, String imageUrl) {
        // TODO: Implémenter
        return null;
    }

    @Override
    public Category updateCategoryOrder(Long id, int displayOrder) {
        // TODO: Implémenter
        return null;
    }

    // ========== DELETE ==========

    @Override
    public void deleteCategory(Long id) {
        log.info("Suppression de la catégorie avec l'ID: {}", id);

        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Catégorie non trouvée avec l'ID: " + id);
        }

        categoryRepository.deleteById(id);
        log.info("Catégorie supprimée avec succès: {}", id);
    }

    @Override
    public void softDeleteCategory(Long id) {
        // TODO: Implémenter
    }

    @Override
    public void reactivateCategory(Long id) {
        // TODO: Implémenter
    }

    // ========== MÉTHODES UTILITAIRES ==========

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return categoryRepository.existsByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public long countCategories() {
        return categoryRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveCategories() {
        // TODO: Implémenter
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> searchCategories(String keyword) {
        // TODO: Implémenter
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public long countServicesByCategory(Long categoryId) {
        // TODO: Implémenter
        return 0;
    }
}