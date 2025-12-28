package com.eadl.connect_backend.infrastructure.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.eadl.connect_backend.domain.model.category.Category;
import com.eadl.connect_backend.domain.port.out.persistence.CategoryRepository;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper.CategoryEntityMapper;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa.CategoryJpaRepository;


@Repository
public class CategoryRepositoryAdapter implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;
    private final CategoryEntityMapper categoryEntityMapper;

    public CategoryRepositoryAdapter(CategoryJpaRepository categoryJpaRepository,
                                     CategoryEntityMapper categoryEntityMapper) {
        this.categoryJpaRepository = categoryJpaRepository;
        this.categoryEntityMapper = categoryEntityMapper;
    }

   

    @Override
    public Category save(Category category) {
        var entity = categoryEntityMapper.toEntity(category);
        var saved = categoryJpaRepository.save(entity);
        return categoryEntityMapper.toModel(saved);
    }

    @Override
    public Optional<Category> findById(Long idCategory) {
        return categoryJpaRepository.findById(idCategory)
                .map(categoryEntityMapper::toModel);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryJpaRepository.findByName(name)
                .map(categoryEntityMapper::toModel);
    }

    @Override
    public List<Category> findAll() {
        return categoryEntityMapper.toModels(categoryJpaRepository.findAll());
    }

    @Override
    public List<Category> findByActive(boolean active) {
        return categoryEntityMapper.toModels(categoryJpaRepository.findByActive(active));
    }

    @Override
    public List<Category> findAllOrderedByDisplayOrder() {
        return categoryEntityMapper.toModels(categoryJpaRepository.findAllByOrderByDisplayOrderAsc());
    }

    @Override
    public boolean existsByName(String name) {
        return categoryJpaRepository.existsByName(name);
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
        if (category.getIdCategory() != null) {
            categoryJpaRepository.deleteById(category.getIdCategory());
        } else {
            // Fallback: try delete by entity
            var entity = categoryEntityMapper.toEntity(category);
            categoryJpaRepository.delete(entity);
        }
    }

}
