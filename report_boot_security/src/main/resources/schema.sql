create table users(username varchar(50) not null primary key,password varchar(500) not null,enabled boolean not null);

create table groups(id bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT, group_name varchar(50) not null unique);

create table group_authorities(group_id bigint(20) NOT NULL, authority varchar(50) not null);

create table group_members(group_id bigint(20) NOT NULL, username varchar(50) not null);
