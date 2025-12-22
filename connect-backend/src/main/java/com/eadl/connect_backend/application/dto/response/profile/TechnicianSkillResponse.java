package com.eadl.connect_backend.application.dto.response.profile;

public class TechnicianSkillResponse {
    
    private Long idSkill;
    private Long idCategory;
    private String categoryName;
    private String skillName;
    private String description;
    private Integer level;
    
    // Getters & Setters
    public Long getIdSkill() {
        return idSkill;
    }
    
    public void setIdSkill(Long idSkill) {
        this.idSkill = idSkill;
    }
    
    public Long getIdCategory() {
        return idCategory;
    }
    
    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getSkillName() {
        return skillName;
    }
    
    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getLevel() {
        return level;
    }
    
    public void setLevel(Integer level) {
        this.level = level;
    }
}