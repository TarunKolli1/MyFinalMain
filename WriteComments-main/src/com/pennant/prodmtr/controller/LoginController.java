package com.pennant.prodmtr.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pennant.prodmtr.model.Entity.User;
import com.pennant.prodmtr.service.Interface.UserService;

@Controller
public class LoginController {

	@Autowired
	UserService userService;

	/**
	 * Displays the login page.
	 *
	 * @param model the model to be populated with data
	 * @return the view name for the login page
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getIndex(Model model) {
		return "login";
	}

	/**
	 * Verifies the user's credentials and determines the appropriate page to redirect.
	 *
	 * @param user    the user object containing login credentials
	 * @param model   the model to be populated with data
	 * @param session the HTTP session to store user information
	 * @return the view name for the appropriate page based on verification result
	 */
	@RequestMapping(value = "/verify", method = RequestMethod.GET)
	public String verifyUser(@Validated User user, Model model, HttpSession session) {
		System.out.println("Users data");
		String page = userService.verifyUser(user, session);
		return page;
	}

	/**
	 * Sets user information in the session and redirects to the "a" page.
	 *
	 * @param model   the model to be populated with data
	 * @param session the HTTP session to store user information
	 * @return the view name for the "a" page
	 */
	@RequestMapping(value = "/a", method = RequestMethod.GET)
	public String a(Model model, HttpSession session) {
		User user = new User();
		user.setUserEmployeeId("1");
		user.setUserDisplayName("bhargav");
		user.setUserPassword("hello");

		session.setAttribute("user", user);
		return "a";
	}

	/**
	 * Sets user information in the session and redirects to the "a" page.
	 *
	 * @param model   the model to be populated with data
	 * @param session the HTTP session to store user information
	 * @return the view name for the "a" page
	 */
	@RequestMapping(value = "getEmail", method = RequestMethod.GET)
	public String getEmail(Model model, HttpSession session) {
		User user = new User();
		user.setUserEmployeeId("1");
		user.setUserDisplayName("bhargav");
		user.setUserPassword("hello");
		session.setAttribute("user", user);
		return "a";
	}
}
