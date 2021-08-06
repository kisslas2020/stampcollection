ALTER TABLE transaction
    DROP COLUMN denom_id, quantity, unit_price;

CREATE TABLE item (
    id serial PRIMARY KEY,
    denom_id bigint,
    quantity bigint,
    unit_price double precision
);

ALTER TABLE item
    ADD FOREIGN KEY (denom_id)
    REFERENCES denomination(id)
    ON DELETE SET NULL;

CREATE TABLE transaction_items(
    transaction_id bigint,
    item_id bigint
);

ALTER TABLE transaction_items
    ADD UNIQUE (item_id);

ALTER TABLE transaction_items
    ADD FOREIGN KEY (transaction_id)
    REFERENCES transaction(id)
    ON DELETE SET NULL;
