
export PGDATA=D:\tools\pgsql\data
pg_ctl init

chcp 65001

Active code page: 65001

D:\tools\pgsql\bin>pg_ctl init
The files belonging to this database system will be owned by user "nfeng".
This user must also own the server process.

The database cluster will be initialized with locale "English_United States.1252".
The default database encoding has accordingly been set to "WIN1252".
The default text search configuration will be set to "english".

Data page checksums are disabled.

fixing permissions on existing directory D:/tools/pgsql/data ... ok
creating subdirectories ... ok
selecting dynamic shared memory implementation ... windows
selecting default max_connections ... 100
selecting default shared_buffers ... 128MB
selecting default time zone ... Asia/Hong_Kong
creating configuration files ... ok
running bootstrap script ... ok
performing post-bootstrap initialization ... ok
syncing data to disk ... ok

initdb: warning: enabling "trust" authentication for local connections
You can change this by editing pg_hba.conf or using the option -A, or
--auth-local and --auth-host, the next time you run initdb.

Success. You can now start the database server using:

    D:/tools/pgsql/bin/pg_ctl -D ^"D^:^\tools^\pgsql^\data^" -l logfile start


D:\tools\pgsql\bin>pg_ctl.exe start
waiting for server to start....2019-12-31 15:10:42.838 HKT [1940] LOG:  starting PostgreSQL 12.1, compiled by Visual C++ build 1914, 64-bit
2019-12-31 15:10:42.887 HKT [1940] LOG:  listening on IPv6 address "::1", port 5432
2019-12-31 15:10:42.889 HKT [1940] LOG:  listening on IPv4 address "127.0.0.1", port 5432
2019-12-31 15:10:43.159 HKT [4716] LOG:  database system was shut down at 2019-12-31 15:09:47 HKT
2019-12-31 15:10:43.291 HKT [1940] LOG:  database system is ready to accept connections
 done
server started

D:\tools\pgsql\bin>psql -d postgres
psql (12.1)
WARNING: Console code page (65001) differs from Windows code page (936)
         8-bit characters might not work correctly. See psql reference
         page "Notes for Windows users" for details.
Type "help" for help.

postgres=#
Active code page: 65001

D:\tools\pgsql\bin>psql -dpostgres
psql (12.1)
WARNING: Console code page (65001) differs from Windows code page (936)
         8-bit characters might not work correctly. See psql reference
         page "Notes for Windows users" for details.
Type "help" for help.

postgres=# create user root superuser password 'root';
CREATE ROLE
postgres=# exit

D:\tools\pgsql\bin>psql -dpostgres -Uroot -W
Password:
psql (12.1)
WARNING: Console code page (65001) differs from Windows code page (936)
         8-bit characters might not work correctly. See psql reference
         page "Notes for Windows users" for details.
Type "help" for help.

postgres=# exit

D:\tools\pgsql\bin>