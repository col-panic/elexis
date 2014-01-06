CREATE TABLE GEMEINDEN (
	BFSNR INTEGER NOT NULL CONSTRAINT bfsnr_no_pk PRIMARY KEY,
	ORTSBEZEICHNUNG VARCHAR(30) NOT NULL,
	KANTON VARCHAR(2)
);

CREATE TABLE PLZ (
	ONRP INTEGER NOT NULL CONSTRAINT onrp_pk PRIMARY KEY,
	BFSNR INTEGER NOT NULL,
	PLZ INTEGER NOT NULL,
	HAUPTPLZ VARCHAR(1)
);

CREATE TABLE STRASSEN (
	EIDGSTRID INTEGER NOT NULL CONSTRAINT eidgstrid_no_pk PRIMARY KEY,
	BFSNR INTEGER CONSTRAINT bfnsr_Strassen_Gemeinden_fk REFERENCES GEMEINDEN(BFSNR),
	STROFFI VARCHAR(50),
	PLZ INTEGER
);

CREATE INDEX PLZ.Plz_bnfsr ON PLZ(PLZ, BFSNR);
CREATE INDEX Strassen_Bfsnr ON STRASSEN(BFSNR);
