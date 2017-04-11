package com.active.dao.impl;

import java.sql.Types;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.stereotype.Repository;

import com.active.dao.AbstractDao;
import com.active.dao.RoleDao;
import com.active.dao.mapper.PermissionMapper;
import com.active.entity.Permission;

@Repository("roleDao")
public class RoleDaoImpl extends AbstractDao implements RoleDao {

	private static final ResourceBundle RB = ResourceBundle.getBundle("sql");

	private static final String GET_PERMISSIONS_BY_ROLE_ID = RB
			.getString("com.active.dao.RoleDao.getPermissionsByRoleId");

	public List<Permission> getPermissions(long roleId) {
		PermissionMapper mapper = new PermissionMapper();
		getJdbcTemplate().query(GET_PERMISSIONS_BY_ROLE_ID,
				new Object[] { roleId }, new int[] { Types.BIGINT }, mapper);
		return mapper.getResults();
	}

}
