package com.active.dao;

import java.util.List;

import com.active.entity.Permission;

public interface RoleDao {
	
	List<Permission> getPermissions(long roleId);

}
