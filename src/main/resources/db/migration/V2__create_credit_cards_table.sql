CREATE TABLE credit_cards (
    id VARCHAR(255) PRIMARY KEY,
    card_number VARCHAR(255) NOT NULL UNIQUE,
    bank_name VARCHAR(255) NOT NULL,
    credit_limit NUMERIC(19,2) NOT NULL,
    expiration_date DATE NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_credit_card_number ON credit_cards(card_number);