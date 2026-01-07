package com.eadl.connect_backend.domain.model.technician;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TechnicianSkillTest {

    @Test
    @DisplayName("Création valide d'une compétence")
    void create_shouldCreateValidSkill() {
        // when
        TechnicianSkill skill = TechnicianSkill.create(
                1L,
                2L,
                "Électricité",
                "Installation électrique",
                3
        );

        // then
        assertNotNull(skill);
        assertEquals(1L, skill.getIdProfile());
        assertEquals(2L, skill.getIdCategory());
        assertEquals("Électricité", skill.getNameSkill());
        assertEquals("Installation électrique", skill.getDescription());
        assertEquals(3, skill.getLevel());
    }

    @Test
    @DisplayName("Création doit échouer si le niveau est invalide")
    void create_shouldThrowException_ifLevelInvalid() {
        // then
        assertThrows(IllegalArgumentException.class, () ->
                TechnicianSkill.create(1L, 2L, "Plomberie", "Test", 0)
        );

        assertThrows(IllegalArgumentException.class, () ->
                TechnicianSkill.create(1L, 2L, "Plomberie", "Test", 6)
        );

        assertThrows(IllegalArgumentException.class, () ->
                TechnicianSkill.create(1L, 2L, "Plomberie", "Test", null)
        );
    }

    @Test
    @DisplayName("Mise à jour de la compétence")
    void updateSkill_shouldUpdateFields() {
        // given
        TechnicianSkill skill = TechnicianSkill.create(
                1L, 2L, "Plomberie", "Ancienne description", 2
        );

        // when
        skill.updateSkill("Plomberie avancée", "Nouvelle description", 4);

        // then
        assertEquals("Plomberie avancée", skill.getNameSkill());
        assertEquals("Nouvelle description", skill.getDescription());
        assertEquals(4, skill.getLevel());
    }

    @Test
    @DisplayName("Upgrade du niveau ne doit pas dépasser 5")
    void upgradeLevel_shouldIncreaseUntilMax() {
        // given
        TechnicianSkill skill = TechnicianSkill.create(
                1L, 2L, "Serrurerie", "Test", 4
        );

        // when
        skill.upgradeLevel();
        skill.upgradeLevel(); // tentative au-dessus de 5

        // then
        assertEquals(5, skill.getLevel());
    }

    @Test
    @DisplayName("Downgrade du niveau ne doit pas descendre sous 1")
    void downgradeLevel_shouldDecreaseUntilMin() {
        // given
        TechnicianSkill skill = TechnicianSkill.create(
                1L, 2L, "Peinture", "Test", 2
        );

        // when
        skill.downgradeLevel();
        skill.downgradeLevel(); // tentative sous 1

        // then
        assertEquals(1, skill.getLevel());
    }

    @Test
    @DisplayName("Une compétence est experte à partir du niveau 4")
    void isExpert_shouldReturnTrueFromLevel4() {
        // given
        TechnicianSkill expertSkill = TechnicianSkill.create(
                1L, 2L, "Climatisation", "Expert", 4
        );

        TechnicianSkill beginnerSkill = TechnicianSkill.create(
                1L, 2L, "Climatisation", "Débutant", 2
        );

        // then
        assertTrue(expertSkill.isExpert());
        assertFalse(beginnerSkill.isExpert());
    }

    @Test
    @DisplayName("Deux compétences sont égales si elles ont le même idSkill")
    void equals_shouldCompareByIdSkill() {
        // given
        TechnicianSkill skill1 = new TechnicianSkill();
        TechnicianSkill skill2 = new TechnicianSkill();

        skill1.setIdSkill(10L);
        skill2.setIdSkill(10L);

        // then
        assertEquals(skill1, skill2);
        assertEquals(skill1.hashCode(), skill2.hashCode());
    }
}
