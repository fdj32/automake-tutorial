package com.active.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.active.entity.Role;

public class RoleMapper extends ListRowMapper<Role> {

	@Override
	public Role mapRow(ResultSet rs) throws SQLException {
		Role role = new Role();
		role.setId(rs.getLong("id"));
		role.setName(rs.getString("name"));
		role.setDescription(rs.getString("description"));
		return role;
	}

}
