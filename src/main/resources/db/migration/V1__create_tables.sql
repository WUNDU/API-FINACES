CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    notification_preference VARCHAR(50) NOT NULL CHECK (notification_preference IN ('EMAIL', 'SMS', 'PUSH')),
    phone VARCHAR(13) UNIQUE NOT NULL CHECK (phone ~ '^\+2449[1-9][0-9]{7}$'),
    login_attempts INT DEFAULT 0,
    locked_until TIMESTAMP
);

CREATE TABLE credit_cards (
    id UUID PRIMARY KEY,
    card_number VARCHAR(255) NOT NULL,
    bank_name VARCHAR(255) NOT NULL,
    credit_limit DECIMAL(15,2) NOT NULL,
    expiration_date DATE NOT NULL,
    user_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);