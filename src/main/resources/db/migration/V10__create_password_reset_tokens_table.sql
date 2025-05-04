-- Criação da tabela password_reset_tokens
CREATE TABLE password_reset_tokens (
    id  varchar(255) PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    user_id varchar(255) NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Índice para melhorar a busca por token
CREATE INDEX idx_password_reset_tokens_token ON password_reset_tokens(token);