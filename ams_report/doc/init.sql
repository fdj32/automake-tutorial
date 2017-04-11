DROP DATABASE ams_shiro;
CREATE DATABASE ams_shiro;
USE ams_shiro;
CREATE TABLE users (
	id bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
	username VARCHAR(64) NOT NULL,
	email VARCHAR(64) NOT NULL,
	password VARCHAR(64) NOT NULL
);

CREATE TABLE roles (
	id bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(64) NOT NULL,
	description VARCHAR(255) NOT NULL
);

CREATE TABLE permissions (
	id bigint(20) NOT NULL PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(64) NOT NULL,
	command VARCHAR(64) NOT NULL,
	description VARCHAR(255) NOT NULL
);

CREATE TABLE users_roles (
	user_id bigint(20),
	role_id bigint(20)
);

CREATE TABLE roles_permissions (
	role_id bigint(20),
	perm_id bigint(20)
);

show tables;
desc users;
desc roles;
desc permissions;
desc users_roles;
desc roles_permissions;

insert into users(id, username, email, password) values (1, 'admin', 'sample@shiro.apache.org', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918');

insert into roles(id, name, description) values (1, 'user', 'The default role given to all users.');
insert into roles(id, name, description) values (2, 'admin', 'The administrator role only given to site admins');

insert into permissions(id, name, command, description) values (1, 'user:*', 'user:*', 'all user operation privileges');

insert into roles_permissions(role_id, perm_id) values (2, 1);

insert into users_roles values (1, 2);


