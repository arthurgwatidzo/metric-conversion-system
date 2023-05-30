package com.technical.assessment.metricconversionsystem;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ConversionTypeEntityDto implements Serializable {
    private final Long id;
    private final String conversionType;
    private final List<UnitConversionDto> unitConversions;

    @Data
    public static class UnitConversionDto implements Serializable {
        private final Long id;
        private final String unitKey;
        private final double conversionFactor;
    }
}
