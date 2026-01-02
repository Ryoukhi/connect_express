package com.eadl.connect_backend.application.service.technician;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eadl.connect_backend.domain.model.technician.TechnicianSkill;
import com.eadl.connect_backend.domain.port.in.technician.TechnicianSkillService;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianSkillRepository;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TechnicianSkillServiceImpl implements TechnicianSkillService {

    private final TechnicianSkillRepository skillRepository;
    private final CurrentUserProvider currentUserProvider;

    // ===================== CREATE =====================
    @Override
    public TechnicianSkill addSkill(TechnicianSkill skill) {
        Long technicianId = currentUserProvider.getCurrentUserId();
        skill.setIdProfile(technicianId);

        // Vérifie qu’il n’y a pas de doublon (même nom + catégorie pour ce technicien)
        boolean exists = skillRepository.existsByProfileIdAndNameSkill(
                technicianId,
                skill.getNameSkill()
        );
        if (exists) {
            throw new IllegalArgumentException(
                    "La compétence existe déjà pour ce technicien"
            );
        }

        return skillRepository.save(skill);
    }

    // ===================== UPDATE =====================
    @Override
    public TechnicianSkill updateSkill(Long skillId, TechnicianSkill updatedSkill) {
        Long technicianId = currentUserProvider.getCurrentUserId();

        TechnicianSkill existing = skillRepository.findById(skillId)
                .orElseThrow(() -> new IllegalArgumentException("Compétence introuvable"));

        // Vérifie que la compétence appartient au technicien connecté
        if (!existing.getIdProfile().equals(technicianId)) {
            throw new SecurityException("Vous ne pouvez pas modifier cette compétence");
        }

        existing.setNameSkill(updatedSkill.getNameSkill() != null ? updatedSkill.getNameSkill() : existing.getNameSkill());
        existing.setDescription(updatedSkill.getDescription() != null ? updatedSkill.getDescription() : existing.getDescription());
        existing.setLevel(updatedSkill.getLevel() != null ? updatedSkill.getLevel() : existing.getLevel());
        existing.setIdCategory(updatedSkill.getIdCategory() != null ? updatedSkill.getIdCategory() : existing.getIdCategory());

        return skillRepository.save(existing);
    }

    // ===================== DELETE =====================
    @Override
    public void deleteSkill(Long skillId, Long technicianId) {
        TechnicianSkill existing = skillRepository.findById(skillId)
                .orElseThrow(() -> new IllegalArgumentException("Compétence introuvable"));

        if (!existing.getIdProfile().equals(technicianId)) {
            throw new SecurityException("Vous ne pouvez pas supprimer cette compétence");
        }

        skillRepository.delete(existing);
    }

    // ===================== READ =====================
    @Override
    @Transactional(readOnly = true)
    public List<TechnicianSkill> getSkillsByTechnicianId(Long technicianId) {
        return skillRepository.findByProfileId(technicianId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TechnicianSkill> getSkillsByCategory(Long technicianId, Long categoryId) {
        return skillRepository.findByProfileIdAndCategoryId(technicianId, categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TechnicianSkill> getSkillById(Long skillId) {
        return skillRepository.findById(skillId);
    }
}