package com.eadl.connect_backend.application.service.category;


import com.eadl.connect_backend.domain.model.category.Category;
import com.eadl.connect_backend.domain.port.in.category.CategoryService;
import com.eadl.connect_backend.domain.port.out.persistence.CategoryRepository;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianSkillRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
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
    private final TechnicianSkillRepository technicianSkillRepository;

    @Override
    public Category createCategory(Category category) {
        log.info("Création de la catégorie: {}", category.getName());

        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la catégorie est obligatoire");
        }

        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Une catégorie avec ce nom existe déjà");
        }

        // Prefer domain factory to ensure defaults
        Category toSave = Category.create(category.getName(), category.getDescription());
        if (category.getIconUrl() != null) {
            toSave.updateIcon(category.getIconUrl());
        }
        if (category.getDisplayOrder() != null) {
            toSave.updateDisplayOrder(category.getDisplayOrder());
        }

        return categoryRepository.save(toSave);
    }

    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        // Return ordered list when available
        try {
            return categoryRepository.findAllOrderedByDisplayOrder();
        } catch (Exception e) {
            log.warn("Impossible d'obtenir les catégories ordonnées, retour à findAll()", e);
            return categoryRepository.findAll();
        }
    }

    @Override
    public List<Category> getActiveCategories() {
        return categoryRepository.findByActive(true);
    }

    @Override
    public Category updateCategoryIcon(Long idCategory, String iconUrl) {
        Category category = categoryRepository.findById(idCategory)
                .orElseThrow(() -> new EntityNotFoundException("Catégorie introuvable"));
        category.updateIcon(iconUrl);
        return categoryRepository.save(category);
    }

    @Override
    public Category updateDisplayOrder(Long idCategory, Integer order) {
        Category category = categoryRepository.findById(idCategory)
                .orElseThrow(() -> new EntityNotFoundException("Catégorie introuvable"));
        category.updateDisplayOrder(order);
        return categoryRepository.save(category);
    }

    @Override
    public Category activateCategory(Long idCategory) {
        Category category = categoryRepository.findById(idCategory)
                .orElseThrow(() -> new EntityNotFoundException("Catégorie introuvable"));
        category.activate();
        return categoryRepository.save(category);
    }

    @Override
    public Category deactivateCategory(Long idCategory) {
        Category category = categoryRepository.findById(idCategory)
                .orElseThrow(() -> new EntityNotFoundException("Catégorie introuvable"));
        category.deactivate();
        return categoryRepository.save(category);
    }

    @Override
    public Long countTechniciansByCategory(Long idCategory) {
        // Count distinct profile IDs in technician skills
        return technicianSkillRepository.findByCategoryId(idCategory)
                .stream()
                .map(s -> s.getIdProfile())
                .distinct()
                .count();
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Catégorie introuvable"));

        if (category.getName() != null && !category.getName().trim().isEmpty()) {
            // check uniqueness if changing name
            if (!existing.getName().equals(category.getName()) && categoryRepository.existsByName(category.getName())) {
                throw new IllegalArgumentException("Une catégorie avec ce nom existe déjà");
            }
        }

        existing.updateCategory(category.getName() == null ? existing.getName() : category.getName(),
                category.getDescription() == null ? existing.getDescription() : category.getDescription());

        if (category.getIconUrl() != null) {
            existing.updateIcon(category.getIconUrl());
        }
        if (category.getDisplayOrder() != null) {
            existing.updateDisplayOrder(category.getDisplayOrder());
        }

        return categoryRepository.save(existing);
    }

    @Override
    public void deleteCategory(Long id) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Catégorie introuvable"));

        // Prevent deletion if technicians are attached to this category
        if (!technicianSkillRepository.findByCategoryId(id).isEmpty()) {
            throw new IllegalStateException("Impossible de supprimer la catégorie car des techniciens y sont rattachés");
        }

        categoryRepository.delete(existing);
        log.info("Catégorie supprimée id={}", id);
    }
}