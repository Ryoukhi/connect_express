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
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TechnicianSkillServiceImpl implements TechnicianSkillService {

    private final TechnicianSkillRepository skillRepository;
    private final CurrentUserProvider currentUserProvider;

    // ===================== CREATE =====================
    @Override
    public TechnicianSkill addSkill(TechnicianSkill skill) {
        Long technicianId = currentUserProvider.getCurrentUserId();
        skill.setIdUser(technicianId);

        log.info("Ajout d'une compétence pour le technicien id={}", technicianId);

        boolean exists = skillRepository.existsByUserIdAndName(
                technicianId,
                skill.getName()
        );
        if (exists) {
            log.error("Compétence déjà existante pour le technicien id={} : {}", technicianId, skill.getName());
            throw new IllegalArgumentException("La compétence existe déjà pour ce technicien");
        }

        TechnicianSkill saved = skillRepository.save(skill);
        log.debug("Compétence ajoutée id={}, nom={}", saved.getIdSkill(), saved.getName());
        return saved;
    }

    // ===================== UPDATE =====================
    @Override
    public TechnicianSkill updateSkill(Long skillId, TechnicianSkill updatedSkill) {
        Long technicianId = currentUserProvider.getCurrentUserId();

        log.info("Mise à jour de la compétence id={} pour le technicien id={}", skillId, technicianId);

        TechnicianSkill existing = skillRepository.findById(skillId)
                .orElseThrow(() -> {
                    log.error("Compétence introuvable id={}", skillId);
                    return new IllegalArgumentException("Compétence introuvable");
                });

        if (!existing.getIdUser().equals(technicianId)) {
            log.error("Tentative non autorisée de modifier la compétence id={} par le technicien id={}", skillId, technicianId);
            throw new SecurityException("Vous ne pouvez pas modifier cette compétence");
        }

        existing.setName(updatedSkill.getName() != null ? updatedSkill.getName() : existing.getName());
        existing.setDescription(updatedSkill.getDescription() != null ? updatedSkill.getDescription() : existing.getDescription());
        existing.setLevel(updatedSkill.getLevel() != null ? updatedSkill.getLevel() : existing.getLevel());
        existing.setIdCategory(updatedSkill.getIdCategory() != null ? updatedSkill.getIdCategory() : existing.getIdCategory());

        TechnicianSkill saved = skillRepository.save(existing);
        log.debug("Compétence mise à jour id={}, nom={}", saved.getIdSkill(), saved.getName());
        return saved;
    }

    // ===================== DELETE =====================
    @Override
    public void deleteSkill(Long skillId, Long technicianId) {
        log.info("Suppression de la compétence id={} par le technicien id={}", skillId, technicianId);

        TechnicianSkill existing = skillRepository.findById(skillId)
                .orElseThrow(() -> {
                    log.error("Compétence introuvable id={}", skillId);
                    return new IllegalArgumentException("Compétence introuvable");
                });

        if (!existing.getIdUser().equals(technicianId)) {
            log.error("Tentative non autorisée de supprimer la compétence id={} par le technicien id={}", skillId, technicianId);
            throw new SecurityException("Vous ne pouvez pas supprimer cette compétence");
        }

        skillRepository.delete(existing);
        log.debug("Compétence supprimée id={}", skillId);
    }

    // ===================== READ =====================
    @Override
    @Transactional(readOnly = true)
    public List<TechnicianSkill> getSkillsByTechnicianId(Long technicianId) {
        log.debug("Récupération des compétences pour le technicien id={}", technicianId);
        List<TechnicianSkill> skills = skillRepository.findByUserId(technicianId);
        log.debug("Nombre de compétences trouvées: {}", skills.size());
        return skills;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TechnicianSkill> getSkillsByCategory(Long technicianId, Long categoryId) {
        log.debug("Récupération des compétences pour le technicien id={} et catégorie id={}", technicianId, categoryId);
        List<TechnicianSkill> skills = skillRepository.findByUserIdAndCategoryId(technicianId, categoryId);
        log.debug("Nombre de compétences trouvées: {}", skills.size());
        return skills;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TechnicianSkill> getSkillById(Long skillId) {
        log.debug("Récupération de la compétence id={}", skillId);
        return skillRepository.findById(skillId);
    }

    // ===================== VERIFY =====================
    @Override
    public TechnicianSkill verifySkill(Long skillId, boolean verified) {
        Long currentUserId = currentUserProvider.getCurrentUserId();
        log.info("Vérification compétence id={} par utilisateur id={} -> {}", skillId, currentUserId, verified);

        // only admins can verify skills
        try {
            if (!currentUserProvider.getCurrentUser().isAdmin()) {
                log.error("Utilisateur id={} non autorisé à vérifier des compétences", currentUserId);
                throw new SecurityException("Accès refusé : opération réservée aux administrateurs");
            }
        } catch (IllegalStateException ise) {
            log.error("Aucun utilisateur authentifié pour vérifier la compétence id={}", skillId);
            throw new SecurityException("Utilisateur non authentifié");
        }

        TechnicianSkill existing = skillRepository.findById(skillId)
                .orElseThrow(() -> {
                    log.error("Compétence introuvable id={}", skillId);
                    return new IllegalArgumentException("Compétence introuvable");
                });

        existing.verifySkill(verified);
        TechnicianSkill saved = skillRepository.save(existing);
        log.debug("Compétence vérifiée id={} verified={}", saved.getIdSkill(), saved.isVerified());
        return saved;
    }
}
