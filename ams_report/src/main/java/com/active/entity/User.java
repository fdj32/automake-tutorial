package com.active.entity;

import java.util.HashSet;
import java.util.Set;

public class User {

	private long id;
	private String username;
	private String email;
	private String password;
	private Set<Role> roles = new HashSet<Role>();
	private Set<Permission> permissions = new HashSet<Permission>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Set<String> getPermissionStrings() {
		if(null == permissions || permissions.isEmpty()) {
			return null;
		}
		Set<String> permStrs = new HashSet<String>();
		for(Permission permission : permissions) {
			permStrs.add(permission.getCommand());
		}
		return permStrs;
	}

}
