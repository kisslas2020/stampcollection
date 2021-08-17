CREATE TABLE stamp (
    id identity(1, 1) PRIMARY KEY,
    name varchar(255),
    year_of_issue integer,
    country VARCHAR(3)
);

CREATE TABLE denomination (
    id identity(1, 1) PRIMARY KEY,
    currency varchar(3),
    stock bigint,
    value double precision,
    stamp_id bigint
);

ALTER TABLE denomination
    ADD FOREIGN KEY (stamp_id)
    REFERENCES stamp(id)
    ON DELETE SET NULL;

CREATE TABLE transaction (
    id identity(1, 1) PRIMARY KEY,
    date_of_transaction date,
    transaction_type varchar
);

ALTER TABLE denomination
    ADD FOREIGN KEY (stamp_id)
    REFERENCES stamp(id)
    ON DELETE SET NULL;

CREATE TABLE item (
    id identity(1, 1) PRIMARY KEY,
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
