package com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.eadl.connect_backend.domain.model.category.Category;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.CategoryEntity;

public class CategoryEntityMapper {

	public static CategoryEntity toEntity(Category model) {
		if (model == null) return null;
		CategoryEntity entity = new CategoryEntity();
		entity.setIdCategory(model.getIdCategory());
		entity.setName(model.getName());
		entity.setDescription(model.getDescription());
		entity.setIconUrl(model.getIconUrl());
		entity.setActive(model.getActive());
		entity.setDisplayOrder(model.getDisplayOrder());
		return entity;
	}

	public static Category toModel(CategoryEntity entity) {
		if (entity == null) return null;
		Category model = Category.create(entity.getName(), entity.getDescription());
		model.setIdCategory(entity.getIdCategory());
		model.setActive(entity.isActive());
		model.updateIcon(entity.getIconUrl());
		model.updateDisplayOrder(entity.getDisplayOrder());
		return model;
	}

	public static List<CategoryEntity> toEntities(List<Category> models) {
		if (models == null) return null;
		return models.stream().map(CategoryEntityMapper::toEntity).collect(Collectors.toList());
	}

	public static List<Category> toModels(List<CategoryEntity> entities) {
		if (entities == null) return null;
		return entities.stream().map(CategoryEntityMapper::toModel).collect(Collectors.toList());
	}

}
