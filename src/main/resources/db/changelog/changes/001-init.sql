CREATE TABLE payment (
    id BIGINT PRIMARY KEY,
    amount NUMERIC(20, 2),
    currency VARCHAR(255),
    from_account VARCHAR(255),
    to_account VARCHAR(255),
    timestamp TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE payment_seq START WITH 50 INCREMENT BY 50;

ALTER TABLE payment ALTER COLUMN id SET DEFAULT nextval('payment_seq');