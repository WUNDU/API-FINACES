ALTER TABLE users
    ADD COLUMN deleted BOOLEAN DEFAULT false;

ALTER TABLE users
    ADD COLUMN data_consent BOOLEAN DEFAULT false;

ALTER TABLE users
    ADD COLUMN created_at TIMESTAMP;

ALTER TABLE users
    ADD COLUMN updated_at TIMESTAMP;


