package com.eadl.connect_backend.infrastructure.adapter.out.persistence.adapter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.eadl.connect_backend.domain.model.technician.AvailabilityStatus;
import com.eadl.connect_backend.domain.model.technician.TechnicianProfile;
import com.eadl.connect_backend.domain.model.technician.TechnicianSearchCriteria;
import com.eadl.connect_backend.domain.port.out.persistence.TechnicianProfileRepository;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.TechnicianProfileEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper.TechnicianProfileEntityMapper;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa.TechnicianProfileJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TechnicianProfileRepositoryAdapter implements TechnicianProfileRepository {

    private final TechnicianProfileJpaRepository jpaRepository;
    private final TechnicianProfileEntityMapper mapper;

    @Override
    public TechnicianProfile save(TechnicianProfile profile) {
        TechnicianProfileEntity entity = mapper.toEntity(profile);
        TechnicianProfileEntity saved = jpaRepository.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    public Optional<TechnicianProfile> findById(Long idProfile) {
        return jpaRepository.findById(idProfile).map(mapper::toModel);
    }

    @Override
    public Optional<TechnicianProfile> findByTechnicianId(Long idTechnician) {
        return jpaRepository.findByTechnicianId(idTechnician).map(mapper::toModel);
    }

    @Override
    public List<TechnicianProfile> findByCategoryId(Long idCategory) {
        return jpaRepository.findByCategoryId(idCategory).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<TechnicianProfile> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<TechnicianProfile> findByVerified(boolean verified) {
        return jpaRepository.findByVerified(verified).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<TechnicianProfile> findByAvailabilityStatus(AvailabilityStatus status) {
        return jpaRepository.findByAvailabilityStatus(status).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<TechnicianProfile> findAvailable() {
        return jpaRepository.findByAvailabilityStatus(AvailabilityStatus.AVAILABLE).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<TechnicianProfile> findByLocationRadius(BigDecimal latitude, BigDecimal longitude, Double radiusKm) {
        return jpaRepository.findByLocationRadius(latitude, longitude, radiusKm).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<TechnicianProfile> findByHourlyRateBetween(BigDecimal minRate, BigDecimal maxRate) {
        return jpaRepository.findByHourlyRateBetween(minRate, maxRate).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<TechnicianProfile> findByAverageRatingGreaterThanEqual(BigDecimal minRating) {
        return jpaRepository.findByAverageRatingGreaterThanEqual(minRating).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<TechnicianProfile> findTopRated(int limit) {
        return jpaRepository.findTopRated(limit).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<TechnicianProfile> findMostExperienced(int limit) {
        return jpaRepository.findMostExperienced(limit).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Long countByVerified(boolean verified) {
        return jpaRepository.countByVerified(verified);
    }

    @Override
    public void delete(TechnicianProfile profile) {
        TechnicianProfileEntity entity = mapper.toEntity(profile);
        jpaRepository.delete(entity);
    }

    @Override
    public Optional<TechnicianProfile> findByIdTechnician(Long technicianId) {
        return jpaRepository.findByTechnicianId(technicianId).map(mapper::toModel);
    }

    @Override
    public List<TechnicianProfile> findByVerifiedFalse() {
        return jpaRepository.findByVerified(false).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<TechnicianProfile> search(TechnicianSearchCriteria criteria) {
        return jpaRepository.search(criteria).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<TechnicianProfile> findTopRatedByCity(String city, boolean verifiedOnly, int limit) {
        return jpaRepository.findTopRatedByCity(city, verifiedOnly, limit).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<TechnicianProfile> findNearbyAvailable(BigDecimal latitude, BigDecimal longitude, BigDecimal radiusKm,
                                                       AvailabilityStatus availabilityStatus) {
        return jpaRepository.findNearbyAvailable(latitude, longitude, radiusKm, availabilityStatus).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }
}