package com.eadl.connect_backend.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eadl.connect_backend.application.dto.CategoryDto;
import com.eadl.connect_backend.domain.model.category.Category;

/**
 * Mapper utilitaire pour les catégories
 */
public class CategoryMapper {

    private CategoryMapper() {
        // Utility class
    }

    public CategoryDto toDto(Category category) {
        if (category == null) return null;

        CategoryDto dto = new CategoryDto();
        dto.setIdCategory(category.getIdCategory());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setIconUrl(category.getIconUrl());
        dto.setActive(category.isActive());
        dto.setDisplayOrder(category.getDisplayOrder());

        return dto;
    }

    public Category toModel(CategoryDto dto) {
        if (dto == null) return null;

        Category category = new Category();
        category.setIdCategory(dto.getIdCategory());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setIconUrl(dto.getIconUrl());
        category.setActive(dto.isActive());
        category.setDisplayOrder(dto.getDisplayOrder());

        return category;
    }

    public List<CategoryDto> toDtoList(List<Category> categories) {
        return categories.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<Category> toModelList(List<CategoryDto> dtos) {
        return dtos.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}