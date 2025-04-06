INSERT INTO rate_providers (provider_code, created_at, modified_at, provider_name, description, priority, active, default_multiplier)
VALUES
    ('CBR', NOW(), NOW(), 'Центробанк России', 'Официальные курсы ЦБ РФ', 10, true, 1.0);
