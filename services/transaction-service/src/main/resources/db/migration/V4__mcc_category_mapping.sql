CREATE TABLE mcc_category_mapping (
    mcc         VARCHAR(4)  PRIMARY KEY,
    category_id BIGINT NOT NULL REFERENCES category (id)
);