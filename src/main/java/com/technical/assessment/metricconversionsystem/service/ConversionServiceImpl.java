package com.technical.assessment.metricconversionsystem.service;

import com.technical.assessment.metricconversionsystem.exceptions.ConversionTypeNotFoundException;
import com.technical.assessment.metricconversionsystem.exceptions.DuplicateUnitConversionException;
import com.technical.assessment.metricconversionsystem.exceptions.UnitConversionNotFoundException;
import com.technical.assessment.metricconversionsystem.model.ConversionTypeEntity;
import com.technical.assessment.metricconversionsystem.model.UnitConversionEntity;
import com.technical.assessment.metricconversionsystem.model.dto.ConversionReqDTO;
import com.technical.assessment.metricconversionsystem.model.dto.NewUnitConversionDTO;
import com.technical.assessment.metricconversionsystem.model.dto.UpdateUnitConversionDTO;
import com.technical.assessment.metricconversionsystem.model.mapper.ConversionTypeEntityMapper;
import com.technical.assessment.metricconversionsystem.repository.ConversionTypeRepository;
import com.technical.assessment.metricconversionsystem.repository.UnitConversionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversionServiceImpl implements ConversionService {

    private final ConversionTypeRepository conversionTypeRepository;
    private final UnitConversionRepository unitConversionRepository;
    private final ConversionTypeEntityMapper conversionMapper;

    @Override
    public double convert(ConversionReqDTO conversionReq) {

        ConversionTypeEntity conversionTypeEntity = conversionTypeRepository.findByConversionType(conversionReq.getConversionType())
                .orElseThrow(() -> new ConversionTypeNotFoundException("Conversion type (%s) does not exist".formatted(conversionReq.getConversionType())));

        List<UnitConversionEntity> unitConversionEntities = conversionTypeEntity.getUnitConversionEntities();
        UnitConversionEntity unitConversionEntity = unitConversionEntities.stream()
                .filter(uc -> uc.getUnitFrom().equals(conversionReq.getUnitFrom()) && uc.getUnitTo().equals(conversionReq.getUnitTo()))
                .findFirst()
                .orElseThrow(() -> new UnitConversionNotFoundException("Unit conversion with From: %s and To: %s does not exist".formatted(conversionReq.getUnitFrom(), conversionReq.getUnitTo())));

        return conversionReq.getValue() * unitConversionEntity.getConversionFactor();
    }

    @Override
    public UnitConversionEntity createUnitConversion(NewUnitConversionDTO newConversionUnit) {

        // check if conversion type is existing
        Optional<ConversionTypeEntity> optionalConversionTypeEntity = conversionTypeRepository.findByConversionType(newConversionUnit.getConversionType());

        // update if conversion type is existing
        if (optionalConversionTypeEntity.isPresent()) {
            ConversionTypeEntity conversionTypeEntity = optionalConversionTypeEntity.get();

            Optional<UnitConversionEntity> existingUnitConversion = conversionTypeEntity.getUnitConversionEntities().stream()
                    .filter(uc -> uc.getUnitFrom().equals(newConversionUnit.getUnitFrom()) && uc.getUnitTo().equals(newConversionUnit.getUnitTo()))
                    .findFirst();

            if (existingUnitConversion.isPresent()) {
                throw new DuplicateUnitConversionException("Unit conversion with From: %s and To: %s parameters already exists".formatted(newConversionUnit.getUnitFrom(), newConversionUnit.getUnitTo()));
            }

            UnitConversionEntity unitConversion = conversionMapper.newConversionUnitDtoToUnitConversionEntity(newConversionUnit);
            unitConversion.setConversionTypeEntity(conversionTypeEntity);

            return unitConversionRepository.save(unitConversion);
        } else {
            // create new conversionTypeEntity
            ConversionTypeEntity newConversionTypeEntity = new ConversionTypeEntity();
            newConversionTypeEntity.setConversionType(newConversionUnit.getConversionType());
            ConversionTypeEntity savedConversionType = conversionTypeRepository.save(newConversionTypeEntity);

            UnitConversionEntity unitConversion = conversionMapper.newConversionUnitDtoToUnitConversionEntity(newConversionUnit);
            unitConversion.setConversionTypeEntity(savedConversionType);

            return unitConversionRepository.save(unitConversion);
        }
    }

    @Override
    public UnitConversionEntity updateUnitConversion(UpdateUnitConversionDTO updateUnitConversionDTO) {

        Optional<ConversionTypeEntity> optionalConversionType = conversionTypeRepository.findByConversionType(updateUnitConversionDTO.getConversionType());

        // check if conversion type is existing
        if (optionalConversionType.isEmpty()) {
            throw new ConversionTypeNotFoundException("Conversion type (%s) does not exist".formatted(updateUnitConversionDTO.getConversionType()));
        }

        ConversionTypeEntity conversionTypeEntity = optionalConversionType.get();

        Optional<UnitConversionEntity> optionalUnitConversionEntity = conversionTypeEntity.getUnitConversionEntities().stream()
                .filter(uc -> uc.getUnitFrom().equals(updateUnitConversionDTO.getUnitFrom()) && uc.getUnitTo().equals(updateUnitConversionDTO.getUnitTo()))
                .findFirst();

        if (optionalUnitConversionEntity.isPresent()) {
            UnitConversionEntity unitConversion = optionalUnitConversionEntity.get();
            unitConversion.setConversionFactor(updateUnitConversionDTO.getConversionFactor());
            return unitConversionRepository.save(unitConversion);

        } else {
            throw new UnitConversionNotFoundException("Invalid unit conversion");
        }
    }

    @Override
    public List<UnitConversionEntity> getUnitConversions() {
        return unitConversionRepository.findAll();
    }

    @Override
    public UnitConversionEntity getUnitConversion(UUID id) {
        return unitConversionRepository.findById(id)
                .orElseThrow(() -> new UnitConversionNotFoundException("UnitConversion with ID: %s not found".formatted(id)));
    }

    @Override
    public List<ConversionTypeEntity> getConversionTypes() {
        return conversionTypeRepository.findAll();
    }

    @Override
    public ConversionTypeEntity getConversionType(UUID id) {
        return conversionTypeRepository.findById(id)
                .orElseThrow(() -> new ConversionTypeNotFoundException("ConversionType with ID: %s not found".formatted(id)));
    }
}
