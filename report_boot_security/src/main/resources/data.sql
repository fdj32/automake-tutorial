insert into users(username, password, enabled) values('user', 'password', true);

insert into groups(id, group_name) values(1, 'USER_GROUP');

insert into group_authorities(group_id, authority) values(1, 'USER_AUTHORITY');

insert into group_members(group_id, username) values(1, 'user');
