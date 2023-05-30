package com.technical.assessment.metricconversionsystem.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
public class ConversionTypeEntityDto implements Serializable {
    private final UUID id;
    private final String conversionType;
    private final List<UnitConversionDto> unitConversions;

    @Data
    public static class UnitConversionDto implements Serializable {
        private final UUID id;
        private final String unitKey;
        private final double conversionFactor;
    }
}
