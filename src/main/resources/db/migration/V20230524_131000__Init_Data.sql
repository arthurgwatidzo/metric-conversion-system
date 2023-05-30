INSERT INTO metric_conversions.conversion_types (id, conversion_type)
VALUES
    ('8f825a1f-dcfa-4e0d-b422-f06156188a06', 'length'),
    ('7be600f2-3971-4e4d-914d-e05de226803e', 'weight'),
    ('15dabfeb-f698-4d80-bcd8-23c25cfbfea6', 'temperature');

INSERT INTO metric_conversions.unit_conversions (id, conversion_type_id, unit_from, unit_to, conversion_factor)
VALUES
    ('aa83a1b6-7b75-40b0-a45a-60471a9461e8', '8f825a1f-dcfa-4e0d-b422-f06156188a06', 'meters', 'feet', 3.28084),
    ('49a49e45-8ef4-4307-aeb2-3454df9cff49', '8f825a1f-dcfa-4e0d-b422-f06156188a06', 'kilometers', 'miles', 0.621371),
    ('7bf4ba23-946d-444a-9014-f3f2476e694b', '7be600f2-3971-4e4d-914d-e05de226803e', 'kilograms', 'pounds', 2.20462),
    ('c743188f-a71a-483f-80ad-6107f0506d02', '7be600f2-3971-4e4d-914d-e05de226803e', 'grams', 'ounces', 0.035274),
    ('3a2470ee-dfff-4f0b-96be-fc0787fba73a', '15dabfeb-f698-4d80-bcd8-23c25cfbfea6', 'celsius', 'fahrenheit', 1.8),
    ('9874daf9-d7ef-4637-9f7b-dc1a25117d83', '15dabfeb-f698-4d80-bcd8-23c25cfbfea6', 'celsius', 'kelvin', 1),
    ('f5d9a618-c529-4b57-b33b-5c7a9d0f84ce', '8f825a1f-dcfa-4e0d-b422-f06156188a06', 'feet', 'meters', 0.3048),
    ('ef4ad3e4-81fd-4b14-a3f7-5b4a76edc725', '8f825a1f-dcfa-4e0d-b422-f06156188a06', 'miles', 'kilometers', 1.60934),
    ('b99187e7-c4a9-4ad2-9c7b-67a7bb8689d1', '7be600f2-3971-4e4d-914d-e05de226803e', 'pounds', 'kilograms', 0.453592),
    ('d234ed25-6e7a-4c4a-8af0-58e09b46a6f0', '7be600f2-3971-4e4d-914d-e05de226803e', 'ounces', 'grams', 28.3495),
    ('2d4cc4b7-b162-4b11-a76b-c6c4d0c2e899', '15dabfeb-f698-4d80-bcd8-23c25cfbfea6', 'fahrenheit', 'celsius', 0.555556),
    ('f2f6de0b-1e01-4133-bd81-527d2b6dc85f', '15dabfeb-f698-4d80-bcd8-23c25cfbfea6', 'kelvin', 'celsius', -273.15);
