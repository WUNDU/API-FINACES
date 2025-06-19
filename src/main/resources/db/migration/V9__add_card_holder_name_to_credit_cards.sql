-- Adicionar coluna card_holder_name à tabela credit_cards
ALTER TABLE credit_cards
ADD COLUMN card_holder_name VARCHAR(50) NOT NULL DEFAULT 'TITULAR DO CARTÃO';

-- Remover o valor default após a migração (opcional)
ALTER TABLE credit_cards
ALTER COLUMN card_holder_name DROP DEFAULT;