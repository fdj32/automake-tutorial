package com.active.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.active.dao.UserDao;
import com.active.entity.Role;
import com.active.entity.User;

@Component
public class SampleRealm extends AuthorizingRealm {

	protected UserDao userDao = null;

	public SampleRealm() {
		setName("SampleRealm");
		// This name must match the name in the User
		// class's getPrincipals() method
		setCredentialsMatcher(new HashedCredentialsMatcher(
				Sha256Hash.ALGORITHM_NAME));
	}

	@Autowired
	public void setUserDAO(UserDao userDao) {
		this.userDao = userDao;
	}

	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		User user = userDao.findUser(token.getUsername());
		if (user != null) {
			return new SimpleAuthenticationInfo(user.getId(),
					user.getPassword(), getName());
		} else {
			return null;
		}
	}

	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		Long userId = (Long) principals.fromRealm(getName()).iterator().next();
		User user = userDao.getUser(userId);
		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			for (Role role : user.getRoles()) {
				info.addRole(role.getName());
				info.addStringPermissions(role.getPermissionStrings());
			}
			return info;
		} else {
			return null;
		}
	}

}
