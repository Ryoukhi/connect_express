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

@Repository
@RequiredArgsConstructor
@Transactional
public class TechnicianProfileRepositoryAdapter implements TechnicianProfileRepository {

    private final TechnicianProfileJpaRepository jpaRepository;
    private final TechnicianProfileEntityMapper mapper;

    @Override
    public Optional<TechnicianProfile> findByTechnicianId(Long idTechnician) {
        return jpaRepository.findByTechnician_IdUser(idTechnician)
                .map(mapper::toModel);
    }

    @Override
    public List<TechnicianProfile> findByCategoryId(Long categoryId) {
        return jpaRepository.findDistinctBySkills_Category_IdCategory(categoryId)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<TechnicianProfile> findByVerified(boolean verified) {
        return jpaRepository.findByVerified(verified)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<TechnicianProfile> findByAvailabilityStatus(AvailabilityStatus status) {
        return jpaRepository.findByAvailabilityStatus(status)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<TechnicianProfile> search(String city, Long categoryId, Boolean verifiedOnly,
                                          Boolean activeOnly, AvailabilityStatus availabilityStatus,
                                          BigDecimal minHourlyRate, BigDecimal maxHourlyRate) {
        return jpaRepository.search(city, categoryId, verifiedOnly, activeOnly,
                                     availabilityStatus, minHourlyRate, maxHourlyRate)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<TechnicianProfile> findTopRatedByCity(String city, boolean verifiedOnly, int limit) {
        return jpaRepository.findTopRatedByCity(city, verifiedOnly, PageRequest.of(0, limit))
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    // @Override
    // public List<TechnicianProfile> findNearbyAvailable(BigDecimal latitude, BigDecimal longitude,
    //                                                    BigDecimal radiusKm, AvailabilityStatus availabilityStatus) {
    //     return jpaRepository.findNearbyAvailable(latitude, longitude, radiusKm, availabilityStatus)
    //             .stream()
    //             .map(mapper::toModel)
    //             .collect(Collectors.toList());
    // }

    @Override
    public TechnicianProfile save(TechnicianProfile profile) {
        TechnicianProfileEntity entity = mapper.toEntity(profile);
        TechnicianProfileEntity saved = jpaRepository.save(entity);
        return mapper.toModel(saved);
    }

    @Override
    public void delete(TechnicianProfile profile) {
        TechnicianProfileEntity entity = mapper.toEntity(profile);
        jpaRepository.delete(entity);
    }

    @Override
    public List<TechnicianProfile> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<TechnicianProfile> findAvailable() {
        return jpaRepository.findByAvailabilityStatus(AvailabilityStatus.AVAILABLE)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<TechnicianProfile> findTopRated(int limit) {
        return jpaRepository.findTopRatedByCity(null, false, PageRequest.of(0, limit))
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }


    @Override
    public Optional<TechnicianProfile> findByIdTechnician(Long technicianId) {
        return jpaRepository.findByTechnician_IdUser(technicianId)
                .map(mapper::toModel);
    }

    @Override
    public List<TechnicianProfile> findByVerifiedFalse() {
        return jpaRepository.findByVerifiedFalse()
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

}