package com.active.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.active.entity.User;
import com.active.service.UserService;

@Component
public class CurrentUserInterceptor extends HandlerInterceptorAdapter {

	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object o,
			ModelAndView modelAndView) throws Exception {
		// Add the current user into the request
		User currentUser = userService.getCurrentUser();
		if (currentUser != null) {
			httpServletRequest.setAttribute("currentUser", currentUser);
		}
	}
}
