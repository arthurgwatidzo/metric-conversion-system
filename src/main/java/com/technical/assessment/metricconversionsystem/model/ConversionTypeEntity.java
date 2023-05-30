package com.technical.assessment.metricconversionsystem.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "conversion_types")
public class ConversionTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "conversion_type")
    private String conversionType;

    @JsonManagedReference
    @OneToMany(mappedBy = "conversionTypeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UnitConversionEntity> unitConversionEntities = new ArrayList<>();

}
