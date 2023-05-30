package com.technical.assessment.metricconversionsystem.repository;

import com.technical.assessment.metricconversionsystem.model.ConversionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConversionTypeRepository extends JpaRepository<ConversionTypeEntity, UUID> {

    Optional<ConversionTypeEntity> findByConversionType(String conversionType);

    boolean existsByConversionType(String conversionType);
}
