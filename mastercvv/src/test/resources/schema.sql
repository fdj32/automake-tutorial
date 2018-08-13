
CREATE SEQUENCE seq_bin START WITH 1 INCREMENT BY 1;

DROP TABLE bin;

CREATE TABLE IF NOT EXISTS bin (
	id INT PRIMARY KEY DEFAULT nextval('seq_bin'),
	bin VARCHAR(32) NOT NULL,
	bank_name VARCHAR(255),
	issuing_network VARCHAR(32),
	country VARCHAR(255),
	card_type VARCHAR(32),
	card_level VARCHAR(32),
	CONSTRAINT uk_bin UNIQUE (bin)
);
