package com.active.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.active.entity.User;
import com.active.service.UserService;

@Controller
public class ManageUsersController {

	private EditUserValidator editUserValidator = new EditUserValidator();

	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping("/manageUsers")
	@RequiresPermissions("user:manage")
	public void manageUsers(Model model) {
		model.addAttribute("users", userService.getAllUsers());
	}

	@RequestMapping(value = "/editUser", method = RequestMethod.GET)
	@RequiresPermissions("user:edit")
	public String showEditUserForm(Model model, @RequestParam Long userId,
			@ModelAttribute EditUserCommand command) {

		User user = userService.getUser(userId);
		command.setUserId(userId);
		command.setUsername(user.getUsername());
		command.setEmail(user.getEmail());
		return "editUser";
	}

	@RequestMapping(value = "/editUser", method = RequestMethod.POST)
	@RequiresPermissions("user:edit")
	public String editUser(Model model, @RequestParam Long userId,
			@ModelAttribute EditUserCommand command, BindingResult errors) {
		editUserValidator.validate(command, errors);

		if (errors.hasErrors()) {
			return "editUser";
		}

		User user = userService.getUser(userId);
		command.updateUser(user);

		userService.updateUser(user);

		return "redirect:/s/manageUsers";
	}

	@RequestMapping("/deleteUser")
	@RequiresPermissions("user:delete")
	public String deleteUser(@RequestParam Long userId) {
		Assert.isTrue(userId != 1, "Cannot delete admin user");
		userService.deleteUser(userId);
		return "redirect:/s/manageUsers";
	}

}
