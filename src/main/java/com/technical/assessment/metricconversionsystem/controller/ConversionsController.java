package com.technical.assessment.metricconversionsystem.controller;

import com.technical.assessment.metricconversionsystem.model.ConversionTypeEntity;
import com.technical.assessment.metricconversionsystem.model.UnitConversionEntity;
import com.technical.assessment.metricconversionsystem.model.dto.ConversionReqDTO;
import com.technical.assessment.metricconversionsystem.model.dto.NewUnitConversionDTO;
import com.technical.assessment.metricconversionsystem.service.ConversionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/conversions")
public class ConversionsController {

    private final ConversionService conversionService;

    @GetMapping(value = "/convert")
    public ResponseEntity<Double> convert(@RequestBody ConversionReqDTO conversionReq) {

        return ResponseEntity.ok(conversionService.convert(conversionReq));
    }

    @GetMapping(value = "/unit-conversions")
    public ResponseEntity<List<UnitConversionEntity>> getUnitConversions() {

        return ResponseEntity.ok(conversionService.getUnitConversions());
    }

    @GetMapping(value = "/unit-conversion/{id}")
    public ResponseEntity<UnitConversionEntity> getUnitConversion(@PathVariable UUID id) {

        return ResponseEntity.ok(conversionService.getUnitConversion(id));
    }

    @GetMapping(value = "/conversion-types")
    public ResponseEntity<List<ConversionTypeEntity>> getConversionTypes() {

        return ResponseEntity.ok(conversionService.getConversionTypes());
    }

    @GetMapping(value = "/conversion-types/{id}")
    public ResponseEntity<ConversionTypeEntity> getConversionType(@PathVariable UUID id) {

        return ResponseEntity.ok(conversionService.getConversionType(id));
    }

    @PostMapping(value = "/create-conversion")
    public ResponseEntity<UnitConversionEntity> createConversion(@RequestBody NewUnitConversionDTO newConversionUnit) {

        return ResponseEntity.ok(conversionService.createUnitConversion(newConversionUnit));
    }

    @PostMapping(value = "/update-conversion")
    public ResponseEntity<UnitConversionEntity> updateConversion(@RequestBody NewUnitConversionDTO newConversionUnit) {

        return ResponseEntity.ok(conversionService.createUnitConversion(newConversionUnit));
    }

}
