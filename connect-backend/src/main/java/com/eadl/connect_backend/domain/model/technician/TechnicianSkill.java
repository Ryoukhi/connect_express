package com.eadl.connect_backend.domain.model.technician;

import java.util.Objects;

/**
 * Compétence du technicien
 */
public class TechnicianSkill {
    private Long idSkill;
    private Long idProfile;
    private Long idCategory;
    private String nameSkill;
    private String description;
    private Integer level; // 1 à 5 (débutant à expert)

    private TechnicianSkill() {}

    // ========== Factory Method ==========
    public static TechnicianSkill create(Long idProfile, Long idCategory, 
                                        String nameSkill, String description, 
                                        Integer level) {
        TechnicianSkill skill = new TechnicianSkill();
        skill.idProfile = idProfile;
        skill.idCategory = idCategory;
        skill.nameSkill = nameSkill;
        skill.description = description;
        skill.level = validateLevel(level);
        return skill;
    }

    // ========== Business Logic Methods ==========
    public void updateSkill(String nameSkill, String description, Integer level) {
        this.nameSkill = nameSkill;
        this.description = description;
        this.level = validateLevel(level);
    }

    public void upgradeLevel() {
        if (this.level < 5) {
            this.level++;
        }
    }

    public void downgradeLevel() {
        if (this.level > 1) {
            this.level--;
        }
    }

    private static Integer validateLevel(Integer level) {
        if (level == null || level < 1 || level > 5) {
            throw new IllegalArgumentException("Le niveau doit être entre 1 et 5");
        }
        return level;
    }

    public boolean isExpert() {
        return this.level >= 4;
    }

    // ========== Getters ==========
    public Long getIdSkill() {
        return idSkill;
    }

    public Long getIdProfile() {
        return idProfile;
    }

    public Long getIdCategory() {
        return idCategory;
    }

    public String getNameSkill() {
        return nameSkill;
    }

    public String getDescription() {
        return description;
    }

    public Integer getLevel() {
        return level;
    }

    // ========== Setters (pour reconstruction depuis DB) ==========
    public void setIdSkill(Long idSkill) {
        this.idSkill = idSkill;
    }

    // ========== equals & hashCode ==========
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TechnicianSkill that = (TechnicianSkill) o;
        return Objects.equals(idSkill, that.idSkill);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSkill);
    }

    @Override
    public String toString() {
        return "TechnicianSkill{" +
                "idSkill=" + idSkill +
                ", nameSkill='" + nameSkill + '\'' +
                ", level=" + level +
                '}';
    }
}