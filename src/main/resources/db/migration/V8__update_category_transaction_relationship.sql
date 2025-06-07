-- Remover coluna transaction_id da tabela categories
ALTER TABLE categories DROP COLUMN IF EXISTS transaction_id;

-- Adicionar coluna category_id na tabela transactions
ALTER TABLE transactions ADD COLUMN IF NOT EXISTS category_id VARCHAR(36);

-- Adicionar constraint de chave estrangeira
ALTER TABLE transactions
    ADD CONSTRAINT fk_transactions_category
    FOREIGN KEY (category_id) REFERENCES categories(id);