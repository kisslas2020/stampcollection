ALTER TABLE transaction
DROP COLUMN
    denomination_id;

ALTER TABLE transaction
DROP COLUMN
    quantity;

ALTER TABLE transaction
DROP COLUMN
    unit_price;

CREATE TABLE item (
    id bigserial PRIMARY KEY,
    denomination_id bigint,
    quantity bigint,
    unit_price double precision,
    transaction_id bigint
);

ALTER TABLE item
    ADD FOREIGN KEY (denomination_id)
    REFERENCES denomination(id)
    ON DELETE SET NULL;

ALTER TABLE item
    ADD FOREIGN KEY (transaction_id)
        REFERENCES transaction(id)
        ON DELETE SET NULL;

CREATE TABLE transaction_items(
    transaction_id bigint,
    item_id bigint
);

ALTER TABLE transaction_items
    ADD UNIQUE (item_id);


