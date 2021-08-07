CREATE TABLE stamp (
    id serial PRIMARY KEY,
    name varchar(255),
    year_of_issue integer
);

CREATE TABLE denomination (
    id serial PRIMARY KEY,
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
    id serial PRIMARY KEY,
    date_of_transaction date,
    denomination_id bigint,
    quantity bigint,
    transaction_type varchar,
    unit_price double precision
);

INSERT INTO stamp (name, year_of_issue) VALUES ('Lepkék', 1980);
INSERT INTO denomination (currency, stock, value, stamp_id) VALUES ('HUF', 1, 10, 1);
INSERT INTO transaction (date_of_transaction, denomination_id, quantity, transaction_type, unit_price) VALUES ('2021-07-30', 1, 12, 'BUY', 50);