package com.eadl.connect_backend.application.service.technician;

import com.eadl.connect_backend.domain.model.technician.TechnicianSkill;
import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianSkillRepository;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnicianSkillServiceImplTest {

    @Mock
    private TechnicianSkillRepository skillRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private TechnicianSkillServiceImpl skillService;

    private TechnicianSkill skill;
    private User adminUser;
    private User normalUser;

    @BeforeEach
    void setUp() {
        skill = new TechnicianSkill();
        skill.setIdSkill(1L);
        skill.setName("Java");
        skill.setIdUser(100L); // Owner ID
        skill.setVerified(false);

        adminUser = mock(User.class);
        normalUser = mock(User.class);
    }

    // ================= ADD =================

    @Test
    void shouldAddSkillSuccessfully() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(100L);
        when(skillRepository.existsByUserIdAndName(100L, "Java")).thenReturn(false);
        when(skillRepository.save(any(TechnicianSkill.class))).thenReturn(skill);

        TechnicianSkill result = skillService.addSkill(skill);

        assertThat(result).isNotNull();
        assertThat(result.getIdUser()).isEqualTo(100L);
        verify(skillRepository).save(any(TechnicianSkill.class));
    }

    @Test
    void shouldThrowIfSkillAlreadyExistsForUser() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(100L);
        when(skillRepository.existsByUserIdAndName(100L, "Java")).thenReturn(true);

        assertThatThrownBy(() -> skillService.addSkill(skill))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("existe déjà");

        verify(skillRepository, never()).save(any());
    }

    // ================= UPDATE =================

    @Test
    void shouldUpdateSkillSuccessfully() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(100L);
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));
        when(skillRepository.save(any(TechnicianSkill.class))).thenReturn(skill);

        TechnicianSkill updateInfo = new TechnicianSkill();
        updateInfo.setName("Java Advanced");

        TechnicianSkill result = skillService.updateSkill(1L, updateInfo);

        assertThat(result.getName()).isEqualTo("Java Advanced");
    }

    @Test
    void shouldThrowIfUpdateNotOwner() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(999L); // Different user
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));

        TechnicianSkill updateInfo = new TechnicianSkill();

        assertThatThrownBy(() -> skillService.updateSkill(1L, updateInfo))
                .isInstanceOf(SecurityException.class);
    }

    @Test
    void shouldThrowIfUpdateSkillNotFound() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(100L);
        when(skillRepository.findById(1L)).thenReturn(Optional.empty());

        TechnicianSkill updateInfo = new TechnicianSkill();

        assertThatThrownBy(() -> skillService.updateSkill(1L, updateInfo))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ================= DELETE =================

    @Test
    void shouldDeleteSkillSuccessfully() {

        // signature is deleteSkill(skillId,
        // technicianId)
        // Wait, looking at code: deleteSkill(Long skillId, Long technicianId)
        // It checks if existing.getIdUser().equals(technicianId)

        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));

        skillService.deleteSkill(1L, 100L);

        verify(skillRepository).delete(skill);
    }

    @Test
    void shouldThrowIfDeleteNotOwner() {
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));

        assertThatThrownBy(() -> skillService.deleteSkill(1L, 999L))
                .isInstanceOf(SecurityException.class);
    }

    // ================= READ =================

    @Test
    void shouldGetSkillsByTechnicianId() {
        when(skillRepository.findByUserId(100L)).thenReturn(List.of(skill));

        List<TechnicianSkill> result = skillService.getSkillsByTechnicianId(100L);

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldGetSkillsByCategory() {
        when(skillRepository.findByUserIdAndCategoryId(100L, 5L)).thenReturn(List.of(skill));

        List<TechnicianSkill> result = skillService.getSkillsByCategory(100L, 5L);

        assertThat(result).hasSize(1);
    }

    @Test
    void shouldGetSkillById() {
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));

        Optional<TechnicianSkill> result = skillService.getSkillById(1L);

        assertThat(result).isPresent();
    }

    // ================= VERIFY =================

    @Test
    void shouldVerifySkillAsAdmin() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(50L);
        when(currentUserProvider.getCurrentUser()).thenReturn(adminUser);
        when(adminUser.isAdmin()).thenReturn(true);
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));
        when(skillRepository.save(any(TechnicianSkill.class))).thenAnswer(i -> i.getArgument(0));

        TechnicianSkill result = skillService.verifySkill(1L, true);

        assertThat(result.isVerified()).isTrue();
    }

    @Test
    void shouldThrowIfVerifyNotAdmin() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(50L);
        when(currentUserProvider.getCurrentUser()).thenReturn(normalUser);
        when(normalUser.isAdmin()).thenReturn(false);

        assertThatThrownBy(() -> skillService.verifySkill(1L, true))
                .isInstanceOf(SecurityException.class);
    }
}
