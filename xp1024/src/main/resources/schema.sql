
mysqld -I -h E:\mysql-8.0.11-winx64\data

ALTER USER "root"@"localhost" IDENTIFIED BY "root";    >>>>>>>>>>>>>>>>>>>>>>>>>>只有这个才行

create database xp1024;

use xp1024;

DROP TABLE htmdata;

CREATE TABLE IF NOT EXISTS htmdata (
	id INT AUTO_INCREMENT PRIMARY KEY,
	fid INT NOT NULL,
	link VARCHAR(50) UNIQUE KEY NOT NULL,
	title VARCHAR(200) NOT NULL,
	data MEDIUMTEXT NOT NULL,
	data_length INT NOT NULL
);

ALTER TABLE htmdata ADD INDEX idx_htmdata_title(title);

desc htmdata;


--ALTER TABLE htmdata ADD INDEX idx_htmdata_link(link);


--------------------------------------------------------------------------------

pg_ctl.exe init -pgdata=E:\tools\pgsql\data

pg_ctl -D E:/tools/pgsql/data start


Success. You can now start the database server using:

    E:/tools/pgsql/bin/pg_ctl -D E:/tools/pgsql/data -l logfile start


E:\tools\pgsql\bin>


taskkill /f /im postgres.exe

E:/tools/pgsql/bin/pg_ctl -D E:/tools/pgsql/data -l logfile stop

pg_ctl -D E:/tools/pgsql/data start

pg_ctl stop

psql -dpostgres


create user root superuser password 'root';


psql -dpostgres -Uroot -W

\c xp1024
\d

psql -dxp1024 -Uroot -W


postgres=# \c xp1024;
用户 root 的口令：
您现在已经连接到数据库 "xp1024",用户 "root".
xp1024=#


xp1024=# \dt
               关联列表
 架构模式 |  名称   |  类型  | 拥有者
----------+---------+--------+--------
 public   | htmdata | 数据表 | root
(1 行记录)


xp1024=# \d htmdata;
                                 数据表 "public.htmdata"
 栏位  |          类型           | Collation | Nullable |            Default

-------+-------------------------+-----------+----------+-----------------------
---------
 id    | integer                 |           | not null | nextval('seq_xp024'::r
egclass)
 fid   | integer                 |           | not null |
 link  | character varying(100)  |           | not null |
 title | character varying(1000) |           | not null |
 data  | text                    |           |          |
索引：
    "htmdata_pkey" PRIMARY KEY, btree (id)
    "htmdata_link_key" UNIQUE CONSTRAINT, btree (link)


xp1024=#

ALTER USER postgres WITH PASSWORD 'postgres';



create database xp1024;

\c xp1024;

CREATE SEQUENCE seq_xp024 START WITH 1 INCREMENT BY 1;

DROP TABLE htmdata;

CREATE TABLE IF NOT EXISTS htmdata (
	id INT PRIMARY KEY DEFAULT nextval('seq_xp024'),
	fid INT NOT NULL,
	link VARCHAR(50) UNIQUE NOT NULL,
	title VARCHAR(200) UNIQUE NOT NULL,
	data TEXT NOT NULL,
	data_length INT NOT NULL
);

\d
\d htmdata

--CREATE INDEX idx_htmdata_title ON htmdata(title);

--alter table htmdata add constraint uk_htmdata_unique_title unique (title);

\d

\dhtmdata

--CREATE INDEX idx_htmdata_link ON htmdata(link);


http://www.jb51.net/article/116677.htm


select * from htmdata a where ((select count(1) from htmdata b where b.link = a.link) > 1) order by a.link desc

SELECT count(1) FROM htmdata WHERE ID in
(SELECT MAX(id) FROM htmdata group by link )

SELECT ID FROM htmdata WHERE ID in
(SELECT MAX(id) FROM htmdata group by link )
order by id

LIMIT 100

DELETE htmdata a WHERE a.id NOT IN ( SELECT MAX(b.id) FROM htmdata b group by b.link )

DELETE FROM htmdata a WHERE a.id NOT IN ( SELECT MAX(b.id) FROM htmdata b group by b.title )

ALTER TABLE htmdata ALTER COLUMN link TYPE VARCHAR(50);  

ALTER TABLE htmdata ALTER COLUMN title TYPE VARCHAR(200);

ALTER TABLE htmdata ADD COLUMN data_length INT;

UPDATE htmdata SET data_length=length(data);


--------------------------------------------------------------------------------

http://www.w3school.com.cn/sql/sql_unique.asp

pg_ctl init

pg_ctl start

pg_ctl -dpostgres

create user root superuser password 'root';

psql -dpostgres -Uroot -W

create database xp1024;

\c xp1024;

CREATE SEQUENCE seq_xp024 START WITH 1 INCREMENT BY 1;

DROP TABLE htmdata;

CREATE TABLE IF NOT EXISTS htmdata (
	id INT PRIMARY KEY DEFAULT nextval('seq_xp024'),
	fid INT NOT NULL,
	link VARCHAR(50) NOT NULL,
	title VARCHAR(200) NOT NULL,
	data TEXT NOT NULL,
	data_length INT NOT NULL,
	CONSTRAINT uk_htmdata_link_title UNIQUE (link,title)
);

CREATE INDEX idx_htmdata_link ON htmdata(link);

CREATE INDEX idx_htmdata_title ON htmdata(title);

\d
\d htmdata


