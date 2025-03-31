INSERT INTO users (id, name, email, password, notification_preference, phone)
VALUES (
    '550e8400-e29b-41d4-a716-446655440000',
    'João Silva',
    'joao@example.com',
    '$2a$10$XURP2lW0v0L3eN2eN2eN2eN2eN2eN2eN2eN2eN2eN2eN2eN2eN2eN', -- senha: minhasenha123
    'EMAIL',
    '+244923456789'
);

INSERT INTO credit_cards (id, card_number, bank_name, credit_limit, expiration_date, user_id)
VALUES (
    '550e8400-e29b-41d4-a716-446655440001',
    'encrypted:1234567812345678',
    'Banco X',
    5000.00,
    '2025-12-31',
    '550e8400-e29b-41d4-a716-446655440000'
);