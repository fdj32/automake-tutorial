package com.active.service;

import java.util.List;

import com.active.entity.Permission;
import com.active.entity.Role;
import com.active.entity.User;

public interface UserService {

	User getCurrentUser();

	void createUser(String username, String email, String password);

	List<User> getAllUsers();

	User getUser(Long userId);

	void deleteUser(Long userId);

	void updateUser(User user);
	
	List<Role> getRolesByUserId(Long userId);
	
	List<Permission> getPermissionsByUserId(Long userId);
}
