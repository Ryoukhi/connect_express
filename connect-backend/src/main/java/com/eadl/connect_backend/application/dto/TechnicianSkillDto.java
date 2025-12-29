package com.eadl.connect_backend.application.dto;

public class TechnicianSkillDto {

    private Long idSkill;
    private Long idProfile;
    private Long idCategory;
    private String nameSkill;
    private String description;
    private Integer level; // 1 à 5

    public Long getIdSkill() {
        return idSkill;
    }
    public void setIdSkill(Long idSkill) {
        this.idSkill = idSkill;
    }
    public Long getIdProfile() {
        return idProfile;
    }
    public void setIdProfile(Long idProfile) {
        this.idProfile = idProfile;
    }
    public Long getIdCategory() {
        return idCategory;
    }
    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
    }
    public String getNameSkill() {
        return nameSkill;
    }
    public void setNameSkill(String nameSkill) {
        this.nameSkill = nameSkill;
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