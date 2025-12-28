package com.eadl.connect_backend.infrastructure.adapter.out.persistence.adapter;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eadl.connect_backend.domain.model.review.Review;
import com.eadl.connect_backend.domain.port.out.persistence.ReviewRepository;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.entity.ReviewEntity;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper.ReviewEntityMapper;
import com.eadl.connect_backend.infrastructure.adapter.out.persistence.repository.jpa.ReviewJpaRepository;
import lombok.RequiredArgsConstructor;

/**
 * Adapter PERSISTENCE - Review
 *
 * Implémentation du port OUT ReviewRepository
 * Responsable de la conversion Domain <-> Entity
 */
@Repository
@RequiredArgsConstructor
@Transactional
public class ReviewRepositoryAdapter implements ReviewRepository {

    private final ReviewJpaRepository reviewJpaRepository;
    private final ReviewEntityMapper reviewEntityMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public Review save(Review review) {
        ReviewEntity entity = reviewEntityMapper.toEntity(review);
        ReviewEntity saved = reviewJpaRepository.save(entity);
        return reviewEntityMapper.toDomain(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Review update(Long idReview, Review review) {
        ReviewEntity existing = reviewJpaRepository.findById(idReview)
            .orElseThrow(() ->
                new IllegalArgumentException("Review not found with id: " + idReview));

        reviewEntityMapper.updateEntity(existing, review);
        ReviewEntity updated = reviewJpaRepository.save(existing);
        return reviewEntityMapper.toDomain(updated);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Review> findById(Long idReview) {
        return reviewJpaRepository.findById(idReview)
            .map(reviewEntityMapper::toDomain);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Review> findByClientId(Long idClient) {
        return reviewJpaRepository.findByClient_IdUser(idClient)
            .stream()
            .map(reviewEntityMapper::toDomain)
            .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Long count() {
        return reviewJpaRepository.count();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Review review) {
        reviewJpaRepository.deleteById(review.getIdReview());
    }
}