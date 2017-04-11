package com.active.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SecurityController {

	private LoginValidator loginValidator = new LoginValidator();

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLoginForm(Model model,
			@ModelAttribute LoginCommand command) {
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(Model model, @ModelAttribute LoginCommand command,
			BindingResult errors) {
		loginValidator.validate(command, errors);

		if (errors.hasErrors()) {
			return showLoginForm(model, command);
		}
System.out.println(command.getUsername());
System.out.println(command.getPassword());
System.out.println(command.isRememberMe());
		UsernamePasswordToken token = new UsernamePasswordToken(
				command.getUsername(), command.getPassword(),
				command.isRememberMe());
		try {
			SecurityUtils.getSubject().login(token);
		} catch (AuthenticationException e) {
			errors.reject("error.login.generic",
					"Invalid username or password.  Please try again.");
		}

		if (errors.hasErrors()) {
			return showLoginForm(model, command);
		} else {
			return "redirect:/s/home";
		}
	}

	@RequestMapping("/logout")
	public String logout() {
		SecurityUtils.getSubject().logout();
		return "redirect:/";
	}

}
