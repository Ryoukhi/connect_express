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

    public static CategoryDto toDto(Category category) {
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

    public static List<CategoryDto> toDtoList(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }
}