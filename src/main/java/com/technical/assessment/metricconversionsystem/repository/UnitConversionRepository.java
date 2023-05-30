package com.technical.assessment.metricconversionsystem.repository;

import com.technical.assessment.metricconversionsystem.model.UnitConversionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UnitConversionRepository extends JpaRepository<UnitConversionEntity, UUID> {
}
