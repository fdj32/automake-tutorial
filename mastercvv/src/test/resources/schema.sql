
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


DROP TABLE id20m;

CREATE TABLE IF NOT EXISTS id20m (
	Name VARCHAR(255),
	CardNo VARCHAR(255),
	Descriot VARCHAR(255),
	CtfTp VARCHAR(255),
	CtfId VARCHAR(255),
	Gender VARCHAR(255),
	Birthday VARCHAR(255),
	Address VARCHAR(255),
	Zip VARCHAR(255),
	Dirty VARCHAR(255),
	District1 VARCHAR(255),
	District2 VARCHAR(255),
	District3 VARCHAR(255),
	District4 VARCHAR(255),
	District5 VARCHAR(255),
	District6 VARCHAR(255),
	FirstNm VARCHAR(255),
	LastNm VARCHAR(255),
	Duty VARCHAR(255),
	Mobile VARCHAR(255),
	Tel VARCHAR(255),
	Fax VARCHAR(255),
	EMail VARCHAR(255),
	Nation VARCHAR(255),
	Taste VARCHAR(255),
	Education VARCHAR(255),
	Company VARCHAR(255),
	CTel VARCHAR(255),
	CAddress VARCHAR(255),
	CZip VARCHAR(255),
	Family VARCHAR(255),
	Version VARCHAR(255),
	id VARCHAR(255) PRIMARY KEY
);

CREATE INDEX idx_id20m_name ON id20m(name);

CREATE INDEX idx_id20m_CtfId ON id20m(CtfId);

\d id20m

insert into id20m(Name,CardNo,Descriot,CtfTp,CtfId,Gender,Birthday,Address,Zip,Dirty,District1,District2,District3,District4,District5,District6,FirstNm,LastNm,Duty,Mobile,Tel,Fax,EMail,Nation,Taste,Education,Company,CTel,CAddress,CZip,Family,Version,id)
values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);





