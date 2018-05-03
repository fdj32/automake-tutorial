
DROP TABLE htmdata;

CREATE TABLE IF NOT EXISTS htmdata (
	id INT AUTO_INCREMENT PRIMARY KEY,
	fid INT NOT NULL,
	link VARCHAR(50) UNIQUE KEY NOT NULL,
	title VARCHAR(200) NOT NULL,
	data TEXT NOT NULL,
	data_length INT NOT NULL
);

--ALTER TABLE htmdata ADD INDEX idx_htmdata_link(link);

ALTER TABLE htmdata ADD INDEX idx_htmdata_title(title);

--------------------------------------------------------------------------------

create database xp1024;



CREATE SEQUENCE seq_xp024 START WITH 1 INCREMENT BY 1;

DROP TABLE htmdata;

CREATE TABLE IF NOT EXISTS htmdata (
	id INT PRIMARY KEY DEFAULT nextval('seq_xp024'),
	fid INT NOT NULL,
	link VARCHAR(50) UNIQUE NOT NULL,
	title VARCHAR(200) NOT NULL,
	data TEXT NOT NULL,
	data_length INT NOT NULL
);

--CREATE INDEX idx_htmdata_link ON htmdata(link);

CREATE INDEX idx_htmdata_title ON htmdata(title);

http://www.jb51.net/article/116677.htm


select * from htmdata a where ((select count(1) from htmdata b where b.link = a.link) > 1) order by a.link desc

SELECT count(1) FROM htmdata WHERE ID in
(SELECT MAX(id) FROM htmdata group by link )

SELECT ID FROM htmdata WHERE ID in
(SELECT MAX(id) FROM htmdata group by link )
order by id

LIMIT 100

DELETE htmdata a WHERE a.id NOT IN ( SELECT MAX(b.id) FROM htmdata b group by b.link )

ALTER TABLE htmdata ALTER COLUMN link TYPE VARCHAR(50);  

ALTER TABLE htmdata ALTER COLUMN title TYPE VARCHAR(200);

ALTER TABLE htmdata ADD COLUMN data_length INT;

UPDATE htmdata SET data_length=length(data);
