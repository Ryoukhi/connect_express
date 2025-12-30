package com.eadl.connect_backend.infrastructure.adapter.out.persistence.mapper;

import com.eadl.connect_backend.domain.model.review.Rating;

import jakarta.persistence.AttributeConverter;
import org.springframework.stereotype.Component;

@Component
public class RatingJpaConverter implements AttributeConverter<Rating, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Rating attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public Rating convertToEntityAttribute(Integer value) {
        return value != null ? Rating.fromValue(value) : null;
    }

}
