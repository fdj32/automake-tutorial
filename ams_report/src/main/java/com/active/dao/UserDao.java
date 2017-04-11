package com.active.dao;

import java.util.List;

import com.active.entity.Permission;
import com.active.entity.Role;
import com.active.entity.User;

public interface UserDao {

	User getUser(Long userId);

	User findUser(String username);

	void createUser(User user);

	List<User> getAllUsers();

	void deleteUser(Long userId);

	void updateUser(User user);
	
	List<Role> getRoles(Long userId);
	
	List<Permission> getPermissions(Long userId);
}
