CREATE TABLE users (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(13) UNIQUE NOT NULL CHECK (phone ~ '^\+2449[1-9][0-9]{7}$'),
    notification_preference VARCHAR(50) NOT NULL CHECK (notification_preference IN ('EMAIL', 'SMS', 'PUSH')),
    plan_type VARCHAR(50) NOT NULL,
    login_attempts INTEGER DEFAULT 0,
    locked BOOLEAN DEFAULT FALSE,
    locked_until TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE,
    data_consent BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
