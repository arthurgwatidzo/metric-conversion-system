package com.technical.assessment.metricconversionsystem.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewUnitConversionDTO {

    @NotEmpty
    private String conversionType;

    @NotEmpty
    private String unitFrom;

    @NotEmpty
    private String unitTo;

    @NotNull
    private Double conversionFactor;
}
