package com.active.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.active.entity.Permission;

public class PermissionMapper extends ListRowMapper<Permission> {

	@Override
	public Permission mapRow(ResultSet rs) throws SQLException {
		Permission permission = new Permission();
		permission.setId(rs.getLong("id"));
		permission.setName(rs.getString("name"));
		permission.setCommand(rs.getString("command"));
		permission.setDescription(rs.getString("description"));
		return permission;
	}

}
