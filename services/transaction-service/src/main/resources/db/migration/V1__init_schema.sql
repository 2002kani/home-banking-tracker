CREATE TABLE IF NOT EXISTS category (
    id         BIGSERIAL PRIMARY KEY,
    user_id    UUID,
    name       VARCHAR(255) NOT NULL,
    color      VARCHAR(50),
    is_system  BOOLEAN,
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS transactions (
    id            BIGSERIAL PRIMARY KEY,
    session_id    VARCHAR(255),
    account_id    VARCHAR(255),
    external_id   VARCHAR(255) UNIQUE,
    user_id       UUID         NOT NULL,
    currency      VARCHAR(10),
    amount        VARCHAR(50),
    creditor_name VARCHAR(255),
    debtor_name   VARCHAR(255),
    type          VARCHAR(10),
    booking_date  DATE,
    status        VARCHAR(10),
    category_id   BIGINT REFERENCES category (id),
    created_at    TIMESTAMP
);
