package com.active.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.active.dao.RoleDao;
import com.active.dao.UserDao;
import com.active.entity.Permission;
import com.active.entity.Role;
import com.active.entity.User;

@Transactional
@Service("userService")
public class UserServiceImpl implements UserService {

    private UserDao userDao;
    
    private RoleDao roleDao;

    @Autowired
    public void setUserDAO(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getCurrentUser() {
        final Long currentUserId = (Long) SecurityUtils.getSubject().getPrincipal();
        if( currentUserId != null ) {
            return getUser(currentUserId);
        } else {
            return null;
        }
    }

    public void createUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword( new Sha256Hash(password).toHex() );
        userDao.createUser( user );
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User getUser(Long userId) {
        return userDao.getUser(userId);
    }

    public void deleteUser(Long userId) {
        userDao.deleteUser( userId );
    }

    public void updateUser(User user) {
        userDao.updateUser( user );
    }

	public List<Role> getRolesByUserId(Long userId) {
		return userDao.getRoles(userId);
	}

	public List<Permission> getPermissionsByUserId(Long userId) {
		List<Permission> list = new ArrayList<Permission>();
		for(Role role : getRolesByUserId(userId)) {
			for(Permission permission : roleDao.getPermissions(role.getId())) {
				list.add(permission);
			}
		}
		return list;
	}

}
