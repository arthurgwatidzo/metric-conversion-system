package com.technical.assessment.metricconversionsystem.model.dto;

import lombok.Data;

@Data
public class ConversionReqDTO {

    private String conversionType;
    private String unitFrom;
    private String unitTo;
    private Double value;
}
