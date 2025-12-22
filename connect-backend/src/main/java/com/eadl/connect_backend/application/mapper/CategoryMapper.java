package com.eadl.connect_backend.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eadl.connect_backend.application.dto.response.category.CategoryResponse;
import com.eadl.connect_backend.domain.model.category.Category;

/**
 * Mapper utilitaire pour les catégories
 */
public class CategoryMapper {

    public static CategoryResponse toResponse(Category category) {
        return toResponse(category, null);
    }

    public static CategoryResponse toResponse(Category category, Long techniciansCount) {
        if (category == null) return null;
        CategoryResponse dto = new CategoryResponse();
        dto.setIdCategory(category.getIdCategory());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setIconUrl(category.getIconUrl());
        dto.setActive(category.getActive());
        dto.setDisplayOrder(category.getDisplayOrder());
        dto.setTechniciansCount(techniciansCount);
        return dto;
    }

    public static List<CategoryResponse> toResponses(List<Category> categories) {
        if (categories == null) return null;
        return categories.stream().map(CategoryMapper::toResponse).collect(Collectors.toList());
    }

    public static List<CategoryResponse> toResponses(List<Category> categories, java.util.Map<Long, Long> countsByCategoryId) {
        if (categories == null) return null;
        return categories.stream().map(c -> toResponse(c, countsByCategoryId != null ? countsByCategoryId.get(c.getIdCategory()) : null)).collect(Collectors.toList());
    }
}
