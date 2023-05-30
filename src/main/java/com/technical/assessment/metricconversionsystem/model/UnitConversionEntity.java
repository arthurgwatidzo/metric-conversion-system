package com.technical.assessment.metricconversionsystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "unit_conversions")
public class UnitConversionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "conversion_type_id")
    private ConversionTypeEntity conversionTypeEntity;

    @Column(name = "unit_from")
    private String unitFrom;

    @Column(name = "unit_To")
    private String unitTo;

    @Column(name = "conversion_factor")
    private double conversionFactor;

}
