CREATE TABLE transactions (
    id VARCHAR(255) PRIMARY KEY,
    amount DOUBLE PRECISION NOT NULL,
    description TEXT,
    type VARCHAR(50) NOT NULL,
    date_time TIMESTAMP NOT NULL,
    credit_card_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (credit_card_id) REFERENCES credit_cards(id)
);