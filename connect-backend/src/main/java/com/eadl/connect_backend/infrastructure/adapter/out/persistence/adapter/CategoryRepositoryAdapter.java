package com.eadl.connect_backend.infrastructure.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;

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

    private final CategoryJpaRepository categoryJpaRepository;
    private final CategoryEntityMapper categoryEntityMapper;

    @Override
    public Category save(Category category) {
        CategoryEntity entity = categoryEntityMapper.toEntity(category);
        CategoryEntity saved = categoryJpaRepository.save(entity);
        return categoryEntityMapper.toModel(saved);
    }

    @Override
    public Optional<Category> findById(Long idCategory) {
        return categoryJpaRepository.findById(idCategory)
                .map(categoryEntityMapper::toModel);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryJpaRepository.findByNameIgnoreCase(name)
                .map(categoryEntityMapper::toModel);
    }

    @Override
    public List<Category> findAll() {
        return categoryJpaRepository.findAll()
                .stream()
                .map(categoryEntityMapper::toModel)
                .toList();
    }

    @Override
    public List<Category> findByActive(boolean active) {
        return categoryJpaRepository.findByActive(active)
                .stream()
                .map(categoryEntityMapper::toModel)
                .toList();
    }

    @Override
    public List<Category> findAllOrderedByDisplayOrder() {
        return categoryJpaRepository.findAllByOrderByDisplayOrderAsc()
                .stream()
                .map(categoryEntityMapper::toModel)
                .toList();
    }

    @Override
    public boolean existsByName(String name) {
        return categoryJpaRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public Long count() {
        return categoryJpaRepository.count();
    }

    @Override
    public Long countByActive(boolean active) {
        return categoryJpaRepository.countByActive(active);
    }

    @Override
    public void delete(Category category) {
        CategoryEntity entity = categoryEntityMapper.toEntity(category);
        categoryJpaRepository.delete(entity);
    }
}
