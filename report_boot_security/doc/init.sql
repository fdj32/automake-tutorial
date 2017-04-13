drop database report;
create database report;
use report;
create table users(username varchar(50) not null primary key,password varchar(500) not null,enabled boolean not null);
create table authorities (username varchar(50) not null,authority varchar(50) not null,constraint fk_authorities_users foreign key(username) references users(username));
create unique index ix_auth_username on authorities (username,authority);

insert into users(username, password, enabled) values('user', 'password', true);
insert into authorities(username, authority) values('user', 'USER_AUTHORITY');


create table groups(id bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT, group_name varchar(50) not null unique);
create table group_authorities(group_id bigint(20) NOT NULL, authority varchar(50) not null);
create table group_members(group_id bigint(20) NOT NULL, username varchar(50) not null);

insert into groups(id, group_name) values(1, 'USER_GROUP');
insert into group_authorities(group_id, authority) values(1, 'USER_AUTHORITY');
insert into group_members(group_id, username) values(1, 'user');

show tables;
desc authorities;
desc group_authorities;
desc group_members;
desc groups;
desc users;   

select * from authorities;
select * from group_authorities;
select * from group_members;
select * from groups;
select * from users;

truncate table authorities;
