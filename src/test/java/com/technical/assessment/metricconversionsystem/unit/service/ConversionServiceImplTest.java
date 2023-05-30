package com.technical.assessment.metricconversionsystem.unit.service;

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
import com.technical.assessment.metricconversionsystem.service.ConversionServiceImpl;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.Mockito.*;

public class ConversionServiceImplTest {

    @Mock
    private ConversionTypeRepository conversionTypeRepository;

    @InjectMocks
    private ConversionServiceImpl conversionService;

    @Mock
    private ConversionTypeEntityMapper conversionMapper;

    @Mock
    private UnitConversionRepository unitConversionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConvert_ValidConversionReqDTO() {
        // Mock conversion type entity and unit conversion entity
        var conversionTypeEntity = new ConversionTypeEntity();
        conversionTypeEntity.setId(UUID.randomUUID());
        conversionTypeEntity.setConversionType("conversionType");

        var unitConversionEntity = new UnitConversionEntity();
        unitConversionEntity.setId(UUID.randomUUID());
        unitConversionEntity.setUnitFrom("unitFrom");
        unitConversionEntity.setUnitTo("unitTo");
        unitConversionEntity.setConversionFactor(2.0);

        conversionTypeEntity.setUnitConversionEntities(List.of(unitConversionEntity));

        // Mock conversion type repository
        when(conversionTypeRepository.findByConversionType(Mockito.anyString()))
                .thenReturn(Optional.of(conversionTypeEntity));

        // Create a conversion request DTO
        ConversionReqDTO conversionReqDTO = new ConversionReqDTO();
        conversionReqDTO.setConversionType("conversionType");
        conversionReqDTO.setUnitFrom("unitFrom");
        conversionReqDTO.setUnitTo("unitTo");
        conversionReqDTO.setValue(5.0);

        // Call the method
        double result = conversionService.convert(conversionReqDTO);

        // Assertion
        Assertions.assertEquals(10.0, result);
    }

    @Test
    public void testConvert_InvalidConversionReqDTO() {
        // Mock conversion type entity
        ConversionTypeEntity conversionTypeEntity = new ConversionTypeEntity();
        conversionTypeEntity.setId(UUID.randomUUID());
        conversionTypeEntity.setConversionType("conversionType");

        // Mock conversion type repository
        when(conversionTypeRepository.findByConversionType(Mockito.anyString()))
                .thenReturn(Optional.of(conversionTypeEntity));

        // Create an invalid conversion request DTO
        ConversionReqDTO conversionReqDTO = new ConversionReqDTO();
        conversionReqDTO.setConversionType("conversionType");
        conversionReqDTO.setUnitFrom("invalidUnitFrom");
        conversionReqDTO.setUnitTo("invalidUnitTo");
        conversionReqDTO.setValue(5.0);

        // Call the method and assert that it throws the expected exception
        Assertions.assertThrows(UnitConversionNotFoundException.class, () -> {
            conversionService.convert(conversionReqDTO);
        });
    }

    @Test
    public void testConvert_ConversionTypeNotFound() {
        // Mock conversion type repository to return an empty optional
        when(conversionTypeRepository.findByConversionType(Mockito.anyString()))
                .thenReturn(Optional.empty());

        // Create a conversion request DTO
        ConversionReqDTO conversionReqDTO = new ConversionReqDTO();
        conversionReqDTO.setConversionType("nonExistingConversionType");
        conversionReqDTO.setUnitFrom("unitFrom");
        conversionReqDTO.setUnitTo("unitTo");
        conversionReqDTO.setValue(5.0);

        // Call the method and assert that it throws the expected exception
        Assertions.assertThrows(ConversionTypeNotFoundException.class, () -> {
            conversionService.convert(conversionReqDTO);
        });
    }

    @Test
    public void testCreateUnitConversion_NewConversionForExistingConversionTypeWithNoExistingUnitConversions_Success() {
        // Create a mock conversion type
        ConversionTypeEntity conversionType = new ConversionTypeEntity();
        conversionType.setConversionType("Length");

        // Create a mock unit conversion for the conversion type
        UnitConversionEntity unitConversionEntity = new UnitConversionEntity();
        unitConversionEntity.setUnitFrom("Meter");
        unitConversionEntity.setUnitTo("Centimeter");
        unitConversionEntity.setConversionFactor(3.7);

        // Set up the mock repository
        when(conversionTypeRepository.findByConversionType(Mockito.anyString())).thenReturn(Optional.of(conversionType));
        when(conversionMapper.newConversionUnitDtoToUnitConversionEntity(Mockito.any())).thenReturn(unitConversionEntity);
        when(unitConversionRepository.save(Mockito.any())).thenReturn(unitConversionEntity);

        // Create a new unit conversion
        NewUnitConversionDTO newConversionUnit = new NewUnitConversionDTO();
        newConversionUnit.setConversionType("Length");
        newConversionUnit.setUnitFrom("Meter");
        newConversionUnit.setUnitTo("Centimeter");
        newConversionUnit.setConversionFactor(3.7);

        // Call the method
        UnitConversionEntity result = conversionService.createUnitConversion(newConversionUnit);

        // Verify the result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(unitConversionEntity, result);
    }

    @Test
    public void testCreateUnitConversion_NewConversionForExistingConversionTypeWithExistingUnitConversion_ThrowException() {
        // Create a mock conversion type
        ConversionTypeEntity conversionType = new ConversionTypeEntity();
        conversionType.setConversionType("Length");

        // Create an existing unit conversion for the conversion type
        UnitConversionEntity existingUnitConversion = new UnitConversionEntity();
        existingUnitConversion.setUnitFrom("Meter");
        existingUnitConversion.setUnitTo("Centimeter");
        conversionType.getUnitConversionEntities().add(existingUnitConversion);

        // Set up the mock repository
        when(conversionTypeRepository.findByConversionType(Mockito.anyString())).thenReturn(Optional.of(conversionType));

        // Create a new unit conversion with the same units as the existing unit conversion
        NewUnitConversionDTO newConversionUnit = new NewUnitConversionDTO();
        newConversionUnit.setConversionType("Length");
        newConversionUnit.setUnitFrom("Meter");
        newConversionUnit.setUnitTo("Centimeter");

        // Call the method and expect an exception
        try {
            conversionService.createUnitConversion(newConversionUnit);
            Assertions.fail("Expected DuplicateUnitConversionException to be thrown");
        } catch (DuplicateUnitConversionException e) {
            // Assertion for the exception message
            Assertions.assertEquals("Unit conversion with From: Meter and To: Centimeter parameters already exists", e.getMessage());
        }
    }

    @Test
    public void testCreateUnitConversion_NewConversionForNonExistingConversionType_Success() {
        // Create a mock conversion type
        ConversionTypeEntity newConversionType = new ConversionTypeEntity();
        newConversionType.setId(UUID.randomUUID());
        newConversionType.setConversionType("Temperature");

        UnitConversionEntity unitConversionEntity = new UnitConversionEntity();
        unitConversionEntity.setId(UUID.randomUUID());
        unitConversionEntity.setConversionTypeEntity(newConversionType);
        unitConversionEntity.setUnitFrom("Celsius");
        unitConversionEntity.setUnitTo("Fahrenheit");
        unitConversionEntity.setConversionFactor(0.2568);

        // Set up the mock repository
        when(conversionTypeRepository.findByConversionType(Mockito.anyString())).thenReturn(Optional.empty());
        when(conversionMapper.newConversionUnitDtoToUnitConversionEntity(Mockito.any())).thenReturn(unitConversionEntity);
        when(conversionTypeRepository.save(Mockito.any())).thenReturn(newConversionType);
        when(unitConversionRepository.save(Mockito.any())).thenReturn(unitConversionEntity);

        // Create a new unit conversion
        NewUnitConversionDTO newConversionUnitDTO = new NewUnitConversionDTO();
        newConversionUnitDTO.setConversionType("Temperature");
        newConversionUnitDTO.setUnitFrom("Celsius");
        newConversionUnitDTO.setUnitTo("Fahrenheit");
        newConversionUnitDTO.setConversionFactor(0.2568);

        // Call the method
        UnitConversionEntity result = conversionService.createUnitConversion(newConversionUnitDTO);

        // Verify the result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(newConversionUnitDTO.getConversionType(), result.getConversionTypeEntity().getConversionType());
    }

    @Test
    public void testUpdateUnitConversion_ExistingUnitConversionWithValidData_Success() {
        // Create a mock conversion type
        ConversionTypeEntity conversionType = new ConversionTypeEntity();
        conversionType.setConversionType("Length");

        // Create a mock unit conversion for the conversion type
        UnitConversionEntity unitConversion = new UnitConversionEntity();
        unitConversion.setUnitFrom("Meter");
        unitConversion.setUnitTo("Centimeter");
        unitConversion.setConversionFactor(100.0);
        conversionType.getUnitConversionEntities().add(unitConversion);

        // Set up the mock repository
        when(conversionTypeRepository.findByConversionType(Mockito.anyString())).thenReturn(Optional.of(conversionType));
        when(unitConversionRepository.save(Mockito.any())).thenReturn(unitConversion);

        // Create an update DTO with valid data
        UpdateUnitConversionDTO updateUnitConversionDTO = new UpdateUnitConversionDTO();
        updateUnitConversionDTO.setConversionType("Length");
        updateUnitConversionDTO.setUnitFrom("Meter");
        updateUnitConversionDTO.setUnitTo("Centimeter");
        updateUnitConversionDTO.setConversionFactor(200.0);

        // Call the method
        UnitConversionEntity result = conversionService.updateUnitConversion(updateUnitConversionDTO);

        // Verify the result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(unitConversion, result);
        Assertions.assertEquals(200.0, result.getConversionFactor(), 0.001);
    }

    @Test
    public void testUpdateUnitConversion_NonExistingConversionType_ThrowException() {
        // Set up the mock repository
        when(conversionTypeRepository.findByConversionType(Mockito.anyString())).thenReturn(Optional.empty());

        // Create an update unit conversion DTO for a non-existing conversion type
        UpdateUnitConversionDTO updateUnitConversionDTO = new UpdateUnitConversionDTO();
        updateUnitConversionDTO.setConversionType("Length");
        updateUnitConversionDTO.setUnitFrom("Meter");
        updateUnitConversionDTO.setUnitTo("Centimeter");
        updateUnitConversionDTO.setConversionFactor(200.00);

        // Call the method and expect an exception
        try {
            conversionService.updateUnitConversion(updateUnitConversionDTO);
            Assertions.fail("Expected ConversionTypeNotFoundException to be thrown");
        } catch (ConversionTypeNotFoundException e) {
            // Assertion for the exception message
            Assertions.assertEquals("Conversion type (Length) does not exist", e.getMessage());
        }
    }

    @Test
    public void testUpdateUnitConversion_NonExistingUnitConversion_ThrowException() {
        // Create a mock conversion type
        ConversionTypeEntity conversionType = new ConversionTypeEntity();
        conversionType.setConversionType("Length");

        // Set up the mock repository
        when(conversionTypeRepository.findByConversionType(Mockito.anyString())).thenReturn(Optional.of(conversionType));

        // Create an update DTO with valid data
        UpdateUnitConversionDTO updateUnitConversionDTO = new UpdateUnitConversionDTO();
        updateUnitConversionDTO.setConversionType("Length");
        updateUnitConversionDTO.setUnitFrom("Meter");
        updateUnitConversionDTO.setUnitTo("Centimeter");
        updateUnitConversionDTO.setConversionFactor(200.0);

        // Call the method and expect an exception
        try {
            conversionService.updateUnitConversion(updateUnitConversionDTO);
            Assertions.fail("Expected UnitConversionNotFoundException to be thrown");
        } catch (UnitConversionNotFoundException e) {
            // Assertion for the exception message
            Assertions.assertEquals("Invalid unit conversion", e.getMessage());
        }
    }

    @Test
    public void testGetUnitConversions() {
        UnitConversionEntity conversion1 = new UnitConversionEntity(UUID.randomUUID(), new ConversionTypeEntity(), "meter", "kilometer", 1.8);
        UnitConversionEntity conversion2 = new UnitConversionEntity(UUID.randomUUID(), new ConversionTypeEntity(), "inch", "centimeter", 2.54);
        List<UnitConversionEntity> conversions = Arrays.asList(conversion1, conversion2);

        when(unitConversionRepository.findAll()).thenReturn(conversions);

        List<UnitConversionEntity> result = conversionService.getUnitConversions();

        Assertions.assertEquals(conversions, result);
        verify(unitConversionRepository).findAll();
    }

    @Test
    public void testGetUnitConversion_ExistingId_ReturnsUnitConversionEntity() {
        // Arrange
        UUID id = UUID.randomUUID();
        UnitConversionEntity expectedConversion = new UnitConversionEntity();
        expectedConversion.setId(id);
        expectedConversion.setConversionTypeEntity(new ConversionTypeEntity());
        expectedConversion.setUnitFrom("celsius");
        expectedConversion.setUnitTo("fahrenheit");
        expectedConversion.setConversionFactor(33.8);

        // Mock the behavior of the repository
        when(unitConversionRepository.findById(id)).thenReturn(Optional.of(expectedConversion));

        // Act
        UnitConversionEntity actualConversion = conversionService.getUnitConversion(id);

        // Assert
        Assertions.assertEquals(expectedConversion, actualConversion);
        verify(unitConversionRepository, times(1)).findById(id);
    }

    @Test
    public void testGetUnitConversion_NonExistingId_ThrowsUnitConversionNotFoundException() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Mock the behavior of the repository
        when(unitConversionRepository.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(UnitConversionNotFoundException.class, () -> conversionService.getUnitConversion(id));
        verify(unitConversionRepository, times(1)).findById(id);
    }


    @Test
    public void testGetConversionTypes() {
        // Prepare test data
        UUID id = UUID.randomUUID();
        ConversionTypeEntity conversionType1 = new ConversionTypeEntity();
        conversionType1.setId(id);
        conversionType1.setConversionType("length");

        UUID id2 = UUID.randomUUID();
        ConversionTypeEntity conversionType2 = new ConversionTypeEntity();
        conversionType2.setId(id2);
        conversionType2.setConversionType("temparature");

        List<ConversionTypeEntity> conversionTypes = new ArrayList<>();
        conversionTypes.add(conversionType1);
        conversionTypes.add(conversionType2);

        // Mock the repository response
        when(conversionTypeRepository.findAll()).thenReturn(conversionTypes);

        // Call the service method
        List<ConversionTypeEntity> result = conversionService.getConversionTypes();

        // Assert the result
        Assertions.assertEquals(conversionTypes.size(), result.size());
        Assertions.assertEquals(conversionType1.getId(), result.get(0).getId());
        Assertions.assertEquals(conversionType1.getConversionType(), result.get(0).getConversionType());
        Assertions.assertEquals(conversionType2.getId(), result.get(1).getId());
        Assertions.assertEquals(conversionType2.getConversionType(), result.get(1).getConversionType());
    }

    @Test
    public void testGetSingleConversionType_Successful() {
        // Prepare test data
        UUID id = UUID.randomUUID();
        ConversionTypeEntity expectedConversionType = new ConversionTypeEntity();
        expectedConversionType.setId(id);
        expectedConversionType.setConversionType("weight");

        // Mock the repository response
        when(conversionTypeRepository.findById(id)).thenReturn(Optional.of(expectedConversionType));

        // Call the service method
        ConversionTypeEntity result = conversionService.getConversionType(id);

        // Assert the result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(id, result.getId());
        Assertions.assertEquals("weight", result.getConversionType());

        // Verify the repository method was called
        verify(conversionTypeRepository, times(1)).findById(id);
    }

    @Test
    public void testGetConversionType_ConversionTypeNotFound() {
        // Prepare test data
        UUID id = UUID.randomUUID();

        // Mock the repository response
        when(conversionTypeRepository.findById(id)).thenReturn(Optional.empty());

        // Call the service method and assert that the exception is thrown
        Assertions.assertThrows(ConversionTypeNotFoundException.class, () -> conversionService.getConversionType(id));

        // Verify the repository method was called
        verify(conversionTypeRepository, times(1)).findById(id);
    }

}

