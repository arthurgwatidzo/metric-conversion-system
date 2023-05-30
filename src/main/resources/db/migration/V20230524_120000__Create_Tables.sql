CREATE TABLE metric_conversions.conversion_types
(
    id              uuid PRIMARY KEY,
    conversion_type VARCHAR(255) NOT NULL
);

CREATE TABLE metric_conversions.unit_conversions
(
    id                uuid PRIMARY KEY,
    conversion_type_id     uuid            NOT NULL,
    unit_from         VARCHAR(255)   NOT NULL,
    unit_to           VARCHAR(255)   NOT NULL,
    conversion_factor DECIMAL(18, 6) NOT NULL,
    FOREIGN KEY (conversion_type_id) REFERENCES conversion_types (id)
);
