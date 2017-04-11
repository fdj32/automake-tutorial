package com.active.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.active.entity.User;

public class UserMapper extends ListRowMapper<User> {

	@Override
	public User mapRow(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getLong("id"));
		user.setUsername(rs.getString("username"));
		user.setPassword(rs.getString("password"));
		user.setEmail(rs.getString("email"));
		return user;
	}

}
