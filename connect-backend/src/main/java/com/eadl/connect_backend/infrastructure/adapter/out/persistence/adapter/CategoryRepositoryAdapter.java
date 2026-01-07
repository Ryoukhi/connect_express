package com.eadl.connect_backend.infrastructure.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.eadl.connect_backend.domain.model.category.Category;
import com.eadl.connect_backend.domain.port.out.persistence.CategoryRepository;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.CategoryEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper.CategoryEntityMapper;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa.CategoryJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepository {

    private static final Logger log = LoggerFactory.getLogger(CategoryRepositoryAdapter.class);

    private final CategoryJpaRepository categoryJpaRepository;
    private final CategoryEntityMapper categoryEntityMapper;

    @Override
    public Category save(Category category) {
        log.info("Saving category: {}", category);

        CategoryEntity entity = categoryEntityMapper.toEntity(category);
        CategoryEntity saved = categoryJpaRepository.save(entity);
        Category savedCategory = categoryEntityMapper.toModel(saved);

        log.info("Saved category: {}", savedCategory);
        return savedCategory;
    }

    @Override
    public Optional<Category> findById(Long idCategory) {
        log.info("Finding category by id: {}", idCategory);

        Optional<Category> category = categoryJpaRepository.findById(idCategory)
                .map(categoryEntityMapper::toModel);

        category.ifPresentOrElse(
                c -> log.info("Found category: {}", c),
                () -> log.warn("No category found with id: {}", idCategory)
        );

        return category;
    }

    @Override
    public Optional<Category> findByName(String name) {
        log.info("Finding category by name: {}", name);

        Optional<Category> category = categoryJpaRepository.findByNameIgnoreCase(name)
                .map(categoryEntityMapper::toModel);

        category.ifPresentOrElse(
                c -> log.info("Found category: {}", c),
                () -> log.warn("No category found with name: {}", name)
        );

        return category;
    }

    @Override
    public List<Category> findAll() {
        log.info("Fetching all categories");

        List<Category> categories = categoryJpaRepository.findAll()
                .stream()
                .map(categoryEntityMapper::toModel)
                .toList();

        log.info("Found {} categories", categories.size());
        return categories;
    }

    @Override
    public List<Category> findByActive(boolean active) {
        log.info("Fetching categories by active = {}", active);

        List<Category> categories = categoryJpaRepository.findByActive(active)
                .stream()
                .map(categoryEntityMapper::toModel)
                .toList();

        log.info("Found {} categories with active = {}", categories.size(), active);
        return categories;
    }

    @Override
    public List<Category> findAllOrderedByDisplayOrder() {
        log.info("Fetching all categories ordered by display order");

        List<Category> categories = categoryJpaRepository.findAllByOrderByDisplayOrderAsc()
                .stream()
                .map(categoryEntityMapper::toModel)
                .toList();

        log.info("Found {} categories ordered by display order", categories.size());
        return categories;
    }

    @Override
    public boolean existsByName(String name) {
        log.info("Checking existence of category with name: {}", name);

        boolean exists = categoryJpaRepository.existsByNameIgnoreCase(name);
        log.info("Category with name '{}' exists: {}", name, exists);

        return exists;
    }

    @Override
    public Long count() {
        log.info("Counting all categories");

        Long total = categoryJpaRepository.count();
        log.info("Total categories: {}", total);

        return total;
    }

    @Override
    public Long countByActive(boolean active) {
        log.info("Counting categories with active = {}", active);

        Long total = categoryJpaRepository.countByActive(active);
        log.info("Total categories with active = {}: {}", active, total);

        return total;
    }

    @Override
    public void delete(Category category) {
        log.info("Deleting category: {}", category);

        CategoryEntity entity = categoryEntityMapper.toEntity(category);
        categoryJpaRepository.delete(entity);

        log.info("Category deleted: {}", category);
    }
}
