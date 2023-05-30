package com.technical.assessment.metricconversionsystem.service;

import com.technical.assessment.metricconversionsystem.model.ConversionTypeEntity;
import com.technical.assessment.metricconversionsystem.model.UnitConversionEntity;
import com.technical.assessment.metricconversionsystem.model.dto.ConversionReqDTO;
import com.technical.assessment.metricconversionsystem.model.dto.NewUnitConversionDTO;
import com.technical.assessment.metricconversionsystem.model.dto.UpdateUnitConversionDTO;

import java.util.List;
import java.util.UUID;

public interface ConversionService {
    double convert(ConversionReqDTO conversionReq);

    UnitConversionEntity createUnitConversion(NewUnitConversionDTO newConversionUnit);

    UnitConversionEntity updateUnitConversion(UpdateUnitConversionDTO updateUnitConversionDTO);

    List<UnitConversionEntity> getUnitConversions();

    UnitConversionEntity getUnitConversion(UUID id);

    List<ConversionTypeEntity> getConversionTypes();

    ConversionTypeEntity getConversionType(UUID id);
}
