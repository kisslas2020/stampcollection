INSERT INTO stamp (name, year_of_issue, country) VALUES ('Butterflies', 2020, 'HUN');
INSERT INTO stamp (name, year_of_issue, country) VALUES ('Countries', 2010, 'HUN');
INSERT INTO stamp (name, year_of_issue, country) VALUES ('Hungarian Scientists', 2000, 'HUN');
INSERT INTO stamp (name, year_of_issue, country) VALUES ('National Holidays', 1990, 'HUN');
INSERT INTO stamp (name, year_of_issue, country) VALUES ('Planets', 1980, 'HUN');

INSERT INTO denomination (currency, stock, value, stamp_id) VALUES ('HUF', 5, 10, 1);
INSERT INTO denomination (currency, stock, value, stamp_id) VALUES ('HUF', 4, 50, 1);
INSERT INTO denomination (currency, stock, value, stamp_id) VALUES ('HUF', 3, 100, 1);
INSERT INTO denomination (currency, stock, value, stamp_id) VALUES ('HUF', 2, 200, 1);
INSERT INTO denomination (currency, stock, value, stamp_id) VALUES ('HUF', 1, 500, 1);
INSERT INTO denomination (currency, stock, value, stamp_id) VALUES ('HUF', 0, 0.5, 3);
INSERT INTO denomination (currency, stock, value, stamp_id) VALUES ('HUF', 8, 2.5, 4);
INSERT INTO denomination (currency, stock, value, stamp_id) VALUES ('HUF', 6, 5.0, 4);
INSERT INTO denomination (currency, stock, value, stamp_id) VALUES ('HUF', 10, 15.0, 4);
INSERT INTO denomination (currency, stock, value, stamp_id) VALUES ('HUF', 10, 0.2, 5);
INSERT INTO denomination (currency, stock, value, stamp_id) VALUES ('HUF', 20, 0.1, 5);

INSERT INTO transaction (date_of_transaction, transaction_type) VALUES ('2021-03-15', 'BUY');
INSERT INTO transaction (date_of_transaction, transaction_type) VALUES ('2021-03-16', 'SELL');

INSERT INTO item (denomination_id, quantity, unit_price, transaction_id) VALUES (1, 15, 25, 1);
INSERT INTO item (denomination_id, quantity, unit_price, transaction_id) VALUES (2, 4, 30, 1);
INSERT INTO item (denomination_id, quantity, unit_price, transaction_id) VALUES (3, 3, 40, 1);
INSERT INTO item (denomination_id, quantity, unit_price, transaction_id) VALUES (4, 2, 50, 1);
INSERT INTO item (denomination_id, quantity, unit_price, transaction_id) VALUES (5, 1, 100, 1);
INSERT INTO item (denomination_id, quantity, unit_price, transaction_id) VALUES (6, 10, 10, 1);
INSERT INTO item (denomination_id, quantity, unit_price, transaction_id) VALUES (7, 15, 8, 1);
INSERT INTO item (denomination_id, quantity, unit_price, transaction_id) VALUES (8, 8, 7, 1);
INSERT INTO item (denomination_id, quantity, unit_price, transaction_id) VALUES (9, 6, 5, 1);
INSERT INTO item (denomination_id, quantity, unit_price, transaction_id) VALUES (10, 10, 2, 1);
INSERT INTO item (denomination_id, quantity, unit_price, transaction_id) VALUES (10, 20, 0.5, 1);
INSERT INTO item (denomination_id, quantity, unit_price, transaction_id) VALUES (1, 10, 35, 2);

INSERT INTO transaction_items (transaction_id, item_id) VALUES (1, 1);
INSERT INTO transaction_items (transaction_id, item_id) VALUES (1, 2);
INSERT INTO transaction_items (transaction_id, item_id) VALUES (1, 3);
INSERT INTO transaction_items (transaction_id, item_id) VALUES (1, 4);
INSERT INTO transaction_items (transaction_id, item_id) VALUES (1, 5);
INSERT INTO transaction_items (transaction_id, item_id) VALUES (1, 6);
INSERT INTO transaction_items (transaction_id, item_id) VALUES (1, 7);
INSERT INTO transaction_items (transaction_id, item_id) VALUES (1, 8);
INSERT INTO transaction_items (transaction_id, item_id) VALUES (1, 9);
INSERT INTO transaction_items (transaction_id, item_id) VALUES (1, 10);
INSERT INTO transaction_items (transaction_id, item_id) VALUES (2, 11);
INSERT INTO transaction_items (transaction_id, item_id) VALUES (2, 12);