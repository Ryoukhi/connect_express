package com.eadl.connect_backend.domain.model.category;

import java.util.Objects;

/**
 * Category - Catégorie de services (Plomberie, Électricité, etc.)
 */
public class Category {
    private Long idCategory;
    private String name;
    private String description;
    private String iconUrl;
    private boolean active;
    private Integer displayOrder;

    public Category() {}

    // ========== Factory Method ==========
    public static Category create(String name, String description) {
        Category category = new Category();
        category.name = name;
        category.description = description;
        category.active = true;
        category.displayOrder = 0;
        return category;
    }

    // ========== Business Logic Methods ==========
    public void updateCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void updateIcon(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void updateDisplayOrder(Integer order) {
        this.displayOrder = order;
    }

    public boolean isActive() {
        return this.active;
    }

    // ========== Getters ==========
    public Long getIdCategory() {
        return idCategory;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public boolean getActive() {
        return active;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    // ========== Setters (pour reconstruction depuis DB) ==========
    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // ========== equals & hashCode ==========
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return idCategory != null && idCategory.equals(category.idCategory);
    }


    @Override
    public int hashCode() {
        return Objects.hash(idCategory, name);
    }

    @Override
    public String toString() {
        return "Category{" +
                "idCategory=" + idCategory +
                ", name='" + name + '\'' +
                ", active=" + active +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
