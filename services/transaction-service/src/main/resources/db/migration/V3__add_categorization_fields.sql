ALTER TABLE transactions ADD COLUMN merchant_category_code VARCHAR(4);
ALTER TABLE transactions ADD COLUMN category_source VARCHAR(16) NOT NULL DEFAULT 'NONE';
ALTER TABLE transactions ADD COLUMN remittance_information TEXT;