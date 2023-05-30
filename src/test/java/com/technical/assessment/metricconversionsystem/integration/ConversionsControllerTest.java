package com.technical.assessment.metricconversionsystem.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technical.assessment.metricconversionsystem.model.ConversionTypeEntity;
import com.technical.assessment.metricconversionsystem.model.UnitConversionEntity;
import com.technical.assessment.metricconversionsystem.model.dto.ConversionReqDTO;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.technical.assessment.metricconversionsystem.model.dto.NewUnitConversionDTO;
import com.technical.assessment.metricconversionsystem.service.ConversionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConversionsControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private ConversionService conversionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testConvert() throws Exception {
        ConversionReqDTO conversionReq = new ConversionReqDTO();
        conversionReq.setConversionType("length");
        conversionReq.setUnitFrom("meter");
        conversionReq.setUnitTo("kilometer");
        conversionReq.setValue(100.0);

        given(conversionService.convert(conversionReq)).willReturn(0.1);

        mockMvc.perform(get("/api/v1/conversions/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(conversionReq)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(0.1));
    }

    @Test
    public void testGetUnitConversions() throws Exception {
        // Create a list of unit conversion entities
        List<UnitConversionEntity> unitConversions = new ArrayList<>();
        // Add some unit conversion entities to the list
        unitConversions.add(new UnitConversionEntity(UUID.randomUUID(),new ConversionTypeEntity(),"meter", "kilometer", 0.001));
        unitConversions.add(new UnitConversionEntity(UUID.randomUUID(),new ConversionTypeEntity(),"pound", "kilogram", 0.453592));

        // Mock the conversionService to return the unitConversions list
        given(conversionService.getUnitConversions()).willReturn(unitConversions);

        // Perform the GET request
        mockMvc.perform(get("/api/v1/conversions/unit-conversions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].unitFrom").value("meter"))
                .andExpect(jsonPath("$[0].unitTo").value("kilometer"))
                .andExpect(jsonPath("$[0].conversionFactor").value(0.001))
                .andExpect(jsonPath("$[1].unitFrom").value("pound"))
                .andExpect(jsonPath("$[1].unitTo").value("kilogram"))
                .andExpect(jsonPath("$[1].conversionFactor").value(0.453592));
    }

    @Test
    public void testGetSingleUnitConversion() throws Exception {
        // Create a list of unit conversion entities
        UUID id = UUID.randomUUID();
        UnitConversionEntity unitConversion = new UnitConversionEntity(id, new ConversionTypeEntity(), "meter", "kilometer", 0.001);

        // Mock the conversionService to return the unitConversions list
        given(conversionService.getUnitConversion(id)).willReturn(unitConversion);

        // Perform the GET request
        mockMvc.perform(get("/api/v1/conversions/unit-conversion/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(String.valueOf(id)))
                .andExpect(jsonPath("$.unitFrom").value("meter"))
                .andExpect(jsonPath("$.unitTo").value("kilometer"))
                .andExpect(jsonPath("$.conversionFactor").value(0.001));
    }

    @Test
    public void testGetConversionTypes() throws Exception {
        List<UnitConversionEntity> unitConversionEntities = new ArrayList<>();

        List<ConversionTypeEntity> conversionTypes = new ArrayList<>();
        conversionTypes.add(new ConversionTypeEntity(UUID.randomUUID(), "length", unitConversionEntities));
        conversionTypes.add(new ConversionTypeEntity(UUID.randomUUID(), "weight", unitConversionEntities));

        given(conversionService.getConversionTypes()).willReturn(conversionTypes);

        mockMvc.perform(get("/api/v1/conversions/conversion-types"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].conversionType").value("length"))
                .andExpect(jsonPath("$[1].conversionType").value("weight"));
    }

    @Test
    public void testCreateUnitConversion_Success() throws Exception {

        NewUnitConversionDTO newUnitConversionDTO = new NewUnitConversionDTO();
        newUnitConversionDTO.setConversionType("length");
        newUnitConversionDTO.setUnitFrom("meter");
        newUnitConversionDTO.setUnitTo("kilometer");
        newUnitConversionDTO.setConversionFactor(1.8);

        UnitConversionEntity newUnitConversion = new UnitConversionEntity(UUID.randomUUID(), new ConversionTypeEntity(), "meter", "kilometer", 1.8);
        given(conversionService.createUnitConversion(newUnitConversionDTO)).willReturn(newUnitConversion);

        mockMvc.perform(post("/api/v1/conversions/create-conversion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(newUnitConversionDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.unitFrom").value("meter"))
                .andExpect(jsonPath("$.unitTo").value("kilometer"))
                .andExpect(jsonPath("$.conversionFactor").value(1.8));
    }

    @Test
    public void testGetSingleConversionType() throws Exception {
        List<UnitConversionEntity> unitConversionEntities = new ArrayList<>();

        UUID id = UUID.randomUUID();
        ConversionTypeEntity conversionType = new ConversionTypeEntity(id, "length", unitConversionEntities);

        given(conversionService.getConversionType(id)).willReturn(conversionType);

        mockMvc.perform(get("/api/v1/conversions/conversion-types/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(String.valueOf(id)))
                .andExpect(jsonPath("$.conversionType").value("length"));
    }

    @Test
    public void testUpdateConversion() throws Exception {
        NewUnitConversionDTO newUnitConversionDTO = new NewUnitConversionDTO();
        newUnitConversionDTO.setConversionType("length");
        newUnitConversionDTO.setUnitFrom("meter");
        newUnitConversionDTO.setUnitTo("kilometer");
        newUnitConversionDTO.setConversionFactor(1.8);

        UnitConversionEntity updatedConversion = new UnitConversionEntity(UUID.randomUUID(), new ConversionTypeEntity(), "meter", "kilometer", 1.8);
        given(conversionService.createUnitConversion(newUnitConversionDTO)).willReturn(updatedConversion);

        mockMvc.perform(post("/api/v1/conversions/update-conversion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(newUnitConversionDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.unitFrom").value("meter"))
                .andExpect(jsonPath("$.unitTo").value("kilometer"))
                .andExpect(jsonPath("$.conversionFactor").value(1.8));
    }

}
