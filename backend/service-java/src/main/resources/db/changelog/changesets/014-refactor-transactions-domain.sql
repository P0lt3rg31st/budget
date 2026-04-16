--liquibase formatted sql

--changeset petrichor:014-refactor-transactions-domain
ALTER TABLE transactions
DROP COLUMN currency;

ALTER TABLE transactions
    RENAME COLUMN merchant_name TO counterparty_name;

ALTER TABLE transactions
DROP CONSTRAINT chk_transactions_amount;

ALTER TABLE transactions
    ADD CONSTRAINT chk_transactions_amount
        CHECK (amount > 0);

ALTER TABLE transactions
    ALTER COLUMN category_id SET NOT NULL;