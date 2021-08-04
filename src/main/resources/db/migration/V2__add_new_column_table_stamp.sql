ALTER TABLE stamp ADD country VARCHAR(3);

UPDATE stamp SET country='HUN' WHERE id=1;