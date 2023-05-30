package com.technical.assessment.metricconversionsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = UnitConversionNotFoundException.class)
    public ResponseEntity<ApiErrorResp> handleUnitConversionNotFoundException(UnitConversionNotFoundException ucnf) {
        ApiErrorResp apiErrorResp = new ApiErrorResp(HttpStatus.NOT_FOUND.value(), ucnf.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorResp);
    }

    @ResponseBody
    @ExceptionHandler(value = ConversionTypeNotFoundException.class)
    public ResponseEntity<ApiErrorResp> handleConversionTypeNotFoundException(ConversionTypeNotFoundException ctnf) {
        ApiErrorResp apiErrorResp = new ApiErrorResp(HttpStatus.NOT_FOUND.value(), ctnf.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorResp);
    }

    @ResponseBody
    @ExceptionHandler(value = DuplicateUnitConversionException.class)
    public ResponseEntity<ApiErrorResp> handleDuplicateUnitConversionException(DuplicateUnitConversionException duce) {
        ApiErrorResp apiErrorResp = new ApiErrorResp(HttpStatus.CONFLICT.value(), duce.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiErrorResp);
    }
}
