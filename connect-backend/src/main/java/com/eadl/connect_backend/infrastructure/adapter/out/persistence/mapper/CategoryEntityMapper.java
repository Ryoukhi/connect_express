package com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.eadl.connect_backend.domain.model.category.Category;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.CategoryEntity;

@Component
public class CategoryEntityMapper {

	public CategoryEntity toEntity(Category model) {
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

	public Category toModel(CategoryEntity entity) {
		if (entity == null) return null;
		Category model = Category.create(entity.getName(), entity.getDescription());
		model.setIdCategory(entity.getIdCategory());
		model.setActive(entity.isActive());
		model.updateIcon(entity.getIconUrl());
		model.updateDisplayOrder(entity.getDisplayOrder());
		return model;
	}

	public List<CategoryEntity> toEntities(List<Category> models) {
		if (models == null) return null;
		return models.stream().map(this::toEntity).collect(Collectors.toList());
	}

	public List<Category> toModels(List<CategoryEntity> entities) {
		if (entities == null) return null;
		return entities.stream().map(this::toModel).collect(Collectors.toList());
	}

}
