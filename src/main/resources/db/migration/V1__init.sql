CREATE TABLE stamp (
    id bigint PRIMARY KEY,
    country varchar(3),
    name varchar(255),
    year_of_issue integer
);

CREATE TABLE denomination (
    id bigint PRIMARY KEY,
    currency varchar(3),
    stock bigint,
    value double precision,
    stamp_id bigint
);

CREATE TABLE transaction (
    id bigint PRIMARY KEY,
    date_of_transaction date,
    denom_id bigint,
    quantity bigint,
    transaction_type varchar,
    unit_price double precision
);

INSERT INTO stamp (id, country, name, year_of_issue) VALUES (1, 'HUN', 'Lepkék', 1980);
INSERT INTO denomination (id, currency, stock, value, stamp_id) VALUES (1, 'HUF', 1, 10, 1);
INSERT INTO transaction (id, date_of_transaction, denom_id, quantity, transaction_type, unit_price) VALUES (1, '2021-07-30', 1, 12, 'BUY', 50);