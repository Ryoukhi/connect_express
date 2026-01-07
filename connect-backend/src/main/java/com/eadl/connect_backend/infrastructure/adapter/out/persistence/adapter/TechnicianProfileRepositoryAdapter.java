package com.eadl.connect_backend.infrastructure.adapter.out.persistence.adapter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;
import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianProfileRepository;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.TechnicianProfileEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper.TechnicianProfileEntityMapper;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa.TechnicianProfileJpaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TechnicianProfileRepositoryAdapter
        implements TechnicianProfileRepository {

    private final TechnicianProfileJpaRepository jpaRepository;
    private final TechnicianProfileEntityMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<TechnicianProfile> findByTechnicianId(Long idTechnician) {
        log.debug("Finding TechnicianProfile by technicianId={}", idTechnician);

        return jpaRepository.findByTechnician_IdUser(idTechnician)
                .map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TechnicianProfile> findByCategoryId(Long categoryId) {
        log.debug("Finding TechnicianProfiles by categoryId={}", categoryId);

        return jpaRepository.findDistinctBySkills_Category_IdCategory(categoryId)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TechnicianProfile> findByVerified(boolean verified) {
        log.debug("Finding TechnicianProfiles by verified={}", verified);

        return jpaRepository.findByVerified(verified)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TechnicianProfile> findByAvailabilityStatus(
            AvailabilityStatus status
    ) {
        log.debug("Finding TechnicianProfiles by availabilityStatus={}", status);

        return jpaRepository.findByAvailabilityStatus(status)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TechnicianProfile> search(
            String city,
            Long categoryId,
            Boolean verifiedOnly,
            Boolean activeOnly,
            AvailabilityStatus availabilityStatus,
            BigDecimal minHourlyRate,
            BigDecimal maxHourlyRate
    ) {
        log.debug(
                "Searching TechnicianProfiles city={}, categoryId={}, verifiedOnly={}, activeOnly={}, availabilityStatus={}, minRate={}, maxRate={}",
                city, categoryId, verifiedOnly, activeOnly,
                availabilityStatus, minHourlyRate, maxHourlyRate
        );

        return jpaRepository.search(
                        city,
                        categoryId,
                        verifiedOnly,
                        activeOnly,
                        availabilityStatus,
                        minHourlyRate,
                        maxHourlyRate
                )
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TechnicianProfile> findTopRatedByCity(
            String city,
            boolean verifiedOnly,
            int limit
    ) {
        log.debug(
                "Finding top rated TechnicianProfiles by city={}, verifiedOnly={}, limit={}",
                city, verifiedOnly, limit
        );

        return jpaRepository
                .findTopRatedByCity(city, verifiedOnly, PageRequest.of(0, limit))
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public TechnicianProfile save(TechnicianProfile profile) {
        log.info("Saving TechnicianProfile");

        TechnicianProfileEntity entity = mapper.toEntity(profile);
        TechnicianProfileEntity saved = jpaRepository.save(entity);

        log.info("TechnicianProfile saved with id={}", saved.getIdProfile());
        return mapper.toModel(saved);
    }

    @Override
    public void delete(TechnicianProfile profile) {
        if (profile.getIdProfile() == null) {
            log.warn("Attempt to delete TechnicianProfile with null id");
            throw new IllegalArgumentException("TechnicianProfile id must not be null");
        }

        log.info("Deleting TechnicianProfile with id={}", profile.getIdProfile());

        TechnicianProfileEntity entity = mapper.toEntity(profile);
        jpaRepository.delete(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TechnicianProfile> findAll() {
        log.debug("Finding all TechnicianProfiles");

        return jpaRepository.findAll()
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TechnicianProfile> findAvailable() {
        log.debug("Finding available TechnicianProfiles");

        return jpaRepository
                .findByAvailabilityStatus(AvailabilityStatus.AVAILABLE)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TechnicianProfile> findTopRated(int limit) {
        log.debug("Finding top rated TechnicianProfiles (global), limit={}", limit);

        return jpaRepository
                .findTopRatedByCity(null, false, PageRequest.of(0, limit))
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TechnicianProfile> findByIdTechnician(Long technicianId) {
        log.debug("Finding TechnicianProfile by technicianId={}", technicianId);

        return jpaRepository.findByTechnician_IdUser(technicianId)
                .map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TechnicianProfile> findByVerifiedFalse() {
        log.debug("Finding unverified TechnicianProfiles");

        return jpaRepository.findByVerifiedFalse()
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }
}
