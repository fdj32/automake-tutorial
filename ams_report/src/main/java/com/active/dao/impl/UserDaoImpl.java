package com.active.dao.impl;

import java.sql.Types;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.stereotype.Repository;

import com.active.dao.AbstractDao;
import com.active.dao.UserDao;
import com.active.dao.mapper.RoleMapper;
import com.active.dao.mapper.UserMapper;
import com.active.entity.Permission;
import com.active.entity.Role;
import com.active.entity.User;

@Repository("userDao")
public class UserDaoImpl extends AbstractDao implements UserDao {

	private static final ResourceBundle RB = ResourceBundle.getBundle("sql");
	private static final String GET_USER = RB
			.getString("com.active.dao.UserDao.getUser");
	private static final String FIND_USER = RB
			.getString("com.active.dao.UserDao.findUser");
	private static final String CREATE_USER = RB
			.getString("com.active.dao.UserDao.createUser");
	private static final String GET_ALL_USER = RB
			.getString("com.active.dao.UserDao.getAllUsers");
	private static final String DELETE_USER = RB
			.getString("com.active.dao.UserDao.deleteUser");
	private static final String UPDATE_USER = RB
			.getString("com.active.dao.UserDao.updateUser");

	private static final String GET_ROLES_BY_USER_ID = RB
			.getString("com.active.dao.UserDao.getRolesByUserId");

	public User getUser(Long userId) {
		UserMapper mapper = new UserMapper();
		getJdbcTemplate().query(GET_USER, new Object[] { userId },
				new int[] { Types.BIGINT }, mapper);
		return mapper.getResult();
	}

	public User findUser(String username) {
		UserMapper mapper = new UserMapper();
		getJdbcTemplate().queryForObject(FIND_USER, new Object[] { username },
				new int[] { Types.VARCHAR }, mapper);
		return mapper.getResult();
	}

	public void createUser(User user) {
		getJdbcTemplate().update(
				CREATE_USER,
				new Object[] { user.getUsername(), user.getEmail(),
						user.getPassword() },
				new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR });
	}

	public List<User> getAllUsers() {
		UserMapper mapper = new UserMapper();
		getJdbcTemplate().query(GET_ALL_USER, mapper);
		return mapper.getResults();
	}

	public void deleteUser(Long userId) {
		getJdbcTemplate().update(DELETE_USER, new Object[] { userId },
				new int[] { Types.BIGINT });
	}

	public void updateUser(User user) {
		getJdbcTemplate().update(
				UPDATE_USER,
				new Object[] { user.getUsername(), user.getEmail(),
						user.getPassword() },
				new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR });
	}

	public List<Role> getRoles(Long userId) {
		RoleMapper mapper = new RoleMapper();
		getJdbcTemplate().query(GET_ROLES_BY_USER_ID, new Object[] { userId },
				new int[] { Types.BIGINT }, mapper);
		return mapper.getResults();
	}

	public List<Permission> getPermissions(Long userId) {
		return null;
	}

}
