
mysqld.exe --initialize-insecure
mysqld.exe

mysql.exe -uroot
alter user 'root'@'localhost' IDENTIFIED BY 'root';
FLUSH PRIVILEGES;

exit
restart

mysql.exe -uroot -proot
