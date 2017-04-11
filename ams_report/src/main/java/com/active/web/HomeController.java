package com.active.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.active.service.UserService;

@Controller
public class HomeController {

	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping("/home")
	public void viewHome(Model model) {
		model.addAttribute("users", userService.getAllUsers());
	}

}
