package com.eadl.connect_backend.application.dto.response.category;

/**
 * DTO pour la réponse catégorie
 */
public class CategoryResponse {
    
    private Long idCategory;
    private String name;
    private String description;
    private String iconUrl;
    private boolean active;
    private Integer displayOrder;
    private Long techniciansCount;
    
    // Getters & Setters
    public Long getIdCategory() {
        return idCategory;
    }
    
    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getIconUrl() {
        return iconUrl;
    }
    
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public Integer getDisplayOrder() {
        return displayOrder;
    }
    
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    public Long getTechniciansCount() {
        return techniciansCount;
    }
    
    public void setTechniciansCount(Long techniciansCount) {
        this.techniciansCount = techniciansCount;
    }
}
