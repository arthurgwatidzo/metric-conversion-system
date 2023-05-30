package com.technical.assessment.metricconversionsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UnitConversionNotFoundException extends RuntimeException {

    public UnitConversionNotFoundException(String message) {
        super(message);
    }
}
