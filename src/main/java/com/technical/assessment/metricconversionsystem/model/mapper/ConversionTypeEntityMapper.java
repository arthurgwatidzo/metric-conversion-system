package com.technical.assessment.metricconversionsystem.model.mapper;

import com.technical.assessment.metricconversionsystem.model.dto.ConversionTypeEntityDto;
import com.technical.assessment.metricconversionsystem.model.ConversionTypeEntity;
import com.technical.assessment.metricconversionsystem.model.UnitConversionEntity;
import com.technical.assessment.metricconversionsystem.model.dto.NewUnitConversionDTO;
import com.technical.assessment.metricconversionsystem.model.dto.UpdateUnitConversionDTO;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ConversionTypeEntityMapper {
    ConversionTypeEntity conversionTypeEntityDtoToConversionTypeEntity(ConversionTypeEntityDto conversionTypeEntityDto);

    ConversionTypeEntityDto conversionTypeEntityToConversionTypeEntityDto(ConversionTypeEntity conversionTypeEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ConversionTypeEntity updateConversionTypeEntityFromConversionTypeEntityDto(ConversionTypeEntityDto conversionTypeEntityDto, @MappingTarget ConversionTypeEntity conversionTypeEntity);

    @AfterMapping
    default void linkUnitConversions(@MappingTarget ConversionTypeEntity conversionTypeEntity) {
        conversionTypeEntity.getUnitConversionEntities().forEach(unitConversion -> unitConversion.setConversionTypeEntity(conversionTypeEntity));
    }

    UnitConversionEntity newConversionUnitDtoToUnitConversionEntity(NewUnitConversionDTO newConversionUnit);

    UnitConversionEntity updateConversionUnitDtoToUnitConversionEntity(UpdateUnitConversionDTO updateUnitConversionDTO);
}
