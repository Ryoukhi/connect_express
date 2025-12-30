package com.eadl.connect_backend.application.service.technician;

import java.util.List;
import java.util.Optional;

import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;
import com.eadl.connect_backend.domain.model.technician.TechnicianSkill;
import com.eadl.connect_backend.domain.model.user.Role;
import com.eadl.connect_backend.domain.model.user.User;
import com.eadl.connect_backend.domain.port.in.technician.TechnicianSkillService;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianProfileRepository;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianSkillRepository;
import com.eadl.connect_backend.domain.port.out.security.CurrentUserProvider;

public class TechnicianSkillServiceImpl implements TechnicianSkillService {

    private final TechnicianSkillRepository skillRepository;
    private final TechnicianProfileRepository profileRepository;
    private final CurrentUserProvider currentUserProvider;

    public TechnicianSkillServiceImpl(
            TechnicianSkillRepository skillRepository,
            TechnicianProfileRepository profileRepository,
            CurrentUserProvider currentUserProvider
    ) {
        this.skillRepository = skillRepository;
        this.profileRepository = profileRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public TechnicianSkill addSkill(TechnicianSkill skill) {

        User currentUser = currentUserProvider.getCurrentUser();

        TechnicianProfile profile = profileRepository
                .findByTechnicianId(currentUser.getIdUser())
                .orElseThrow(() -> new IllegalStateException("Technician profile not found"));

        // Sécurité : un technicien ne peut ajouter que sur son profil
        skill.setIdProfile(profile.getIdProfile());

        if (skillRepository.existsByProfileIdAndNameSkill(
                profile.getIdProfile(),
                skill.getNameSkill()
        )) {
            throw new IllegalArgumentException("Skill already exists for this technician");
        }

        return skillRepository.save(skill);
    }

    @Override
    public TechnicianSkill updateSkill(TechnicianSkill skill) {

        TechnicianSkill existingSkill = skillRepository.findById(skill.getIdSkill())
                .orElseThrow(() -> new IllegalArgumentException("Skill not found"));

        validateOwnership(existingSkill.getIdProfile());

        existingSkill.setNameSkill(skill.getNameSkill());
        existingSkill.setDescription(skill.getDescription());
        existingSkill.setLevel(skill.getLevel());
        existingSkill.setIdCategory(skill.getIdCategory());

        return skillRepository.save(existingSkill);
    }

    @Override
    public void removeSkill(Long skillId) {

        TechnicianSkill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new IllegalArgumentException("Skill not found"));

        validateOwnership(skill.getIdProfile());

        skillRepository.delete(skill);
    }

    @Override
    public List<TechnicianSkill> getSkillsByTechnician(Long technicianId) {

        TechnicianProfile profile = profileRepository
                .findByTechnicianId(technicianId)
                .orElseThrow(() -> new IllegalArgumentException("Technician profile not found"));

        return skillRepository.findByProfileId(profile.getIdProfile());
    }

    @Override
    public List<TechnicianSkill> getSkillsByCategory(Long technicianId, Long categoryId) {

        TechnicianProfile profile = profileRepository
                .findByTechnicianId(technicianId)
                .orElseThrow(() -> new IllegalArgumentException("Technician profile not found"));

        return skillRepository.findByProfileIdAndCategoryId(
                profile.getIdProfile(),
                categoryId
        );
    }

    @Override
    public Optional<TechnicianSkill> getSkillById(Long skillId) {
        return skillRepository.findById(skillId);
    }

    /**
     * Vérifie que l’utilisateur connecté est autorisé à modifier le profil
     */
    private void validateOwnership(Long profileId) {

        User currentUser = currentUserProvider.getCurrentUser();

        if (currentUser.getRole() == Role.ADMIN) {
            return;
        }

        TechnicianProfile profile = profileRepository
                .findById(profileId)
                .orElseThrow(() -> new IllegalStateException("Profile not found"));

        if (!profile.getIdTechnician().equals(currentUser.getIdUser())) {
            throw new SecurityException("Access denied");
        }
    }
}