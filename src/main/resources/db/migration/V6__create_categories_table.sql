CREATE TABLE categories (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    transaction_id VARCHAR(36),
    FOREIGN KEY (transaction_id) REFERENCES transactions(id)
);