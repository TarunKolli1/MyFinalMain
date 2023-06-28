package com.pennant.prodmtr.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.pennant.prodmtr.model.Entity.Subtask;
import com.pennant.prodmtr.model.Entity.Task;
import com.pennant.prodmtr.model.Entity.User;
import com.pennant.prodmtr.service.Interface.UserService;

@Controller
public class ProductivityController {
	UserService userService;

	public static final Logger logger = LoggerFactory.getLogger(ProductivityController.class);

	@Autowired
	public ProductivityController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Retrieves the index page for productivity.
	 *
	 * @param model the model to be populated with data
	 * @return the view name for the productivity index page
	 */
	@RequestMapping(value = "/productivity", method = RequestMethod.GET)
	public String getIndex(Model model) {
		logger.info("Main page has been loading....");
		return "productivity";
	}

	/**
	 * Retrieves the alternative index page for productivity.
	 *
	 * @param model the model to be populated with data
	 * @return the view name for the alternative productivity index page
	 */
	@RequestMapping(value = "/prod", method = RequestMethod.GET)
	public String getIndexAlternative(Model model) {
		logger.info("Landing page has been loaded");
		return "productivity";
	}

	/**
	 * Retrieves the dashboard page.
	 *
	 * @param model the model to be populated with data
	 * @return the view name for the dashboard page
	 */
	@RequestMapping(value = "/dash", method = RequestMethod.GET)
	public String dash(Model model) {
		logger.info("The dashboard has been displayed");
		return "dashboard";
	}

	/**
	 * Retrieves the past due page.
	 *
	 * @param model the model to be populated with data
	 * @return the view name for the past due page
	 */
	@RequestMapping(value = "/pastdue", method = RequestMethod.GET)
	public String pastdue(Model model) {
		logger.info("PastDue data will be displayed here");
		return "pastdue";
	}

	/**
	 * Retrieves the resource page.
	 *
	 * @param model the model to be populated with data
	 * @return the view name for the resource page
	 */
	@RequestMapping(value = "/resource", method = RequestMethod.GET)
	public String resource(Model model) {
		logger.info("Resources page has been fetched");
		return "AddResource";
	}

	/**
	 * Retrieves the project page.
	 *
	 * @param model the model to be populated with data
	 * @return the view name for the project page
	 */
	@RequestMapping(value = "/project", method = RequestMethod.GET)
	public String project(Model model) {
		logger.info("Projects has been fetched");
		return "project";
	}

	/**
	 * Retrieves the analytics page.
	 *
	 * @param model the model to be populated with data
	 * @return the view name for the analytics page
	 */
	@RequestMapping(value = "/analytics", method = RequestMethod.GET)
	public String getanalytics(Model model) {
		logger.info("Analytics has been called");
		return "analytics";
	}

	/**
	 * Retrieves the backlog page.
	 *
	 * @param model the model to be populated with data
	 * @return the view name for the backlog page
	 */
	@RequestMapping(value = "/backlog", method = RequestMethod.GET)
	public String getmodules(Model model) {
		logger.info("Backlogs has been fetched");
		return "backlog";
	}

	/**
	 * Retrieves the profile page.
	 *
	 * @param model the model to be populated with data
	 * @return the view name for the profile page
	 */
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String getprofile(Model model) {
		logger.info("Profile has been requested");
		return "profile";
	}

	/**
	 * Retrieves the edit page.
	 *
	 * @param model the model to be populated with data
	 * @param id    the user id
	 * @return the view name for the edit page
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String getedit(Model model, @RequestParam("id") Integer id) {
		model.addAttribute("id", id);
		logger.info("edit requested by the : {}", id);
		return "edit";
	}

	/**
	 * Updates the user password and retrieves the edit success page.
	 *
	 * @param model    the model to be populated with data
	 * @param id       the user id
	 * @param Password the new password
	 * @return the view name for the edit success page
	 */
	@RequestMapping(value = "/editsuccess", method = RequestMethod.POST)
	public String geteditsuccess(Model model, @RequestParam("id") Integer id,
			@RequestParam("Password") String Password) {
		userService.UpdatePassword(id, Password);
		model.addAttribute("id", id);
		logger.info("Updated the details for id:{}", id);
		return "edit";
	}

	/**
	 * Retrieves the activity page.
	 *
	 * @param model   the model to be populated with data
	 * @param session the HTTP session
	 * @return the view name for the activity page
	 */
	@RequestMapping(value = "/activity", method = RequestMethod.GET)
	public String getActivity(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		List<Task> activiyTasks = userService.getUserActivities(user.getUserRole());
		List<Subtask> activiySubTasks = userService.getUserSubtaskActivities(user.getUserRole());
		model.addAttribute("activityTasks", activiyTasks);
		model.addAttribute("activitySubTasks", activiySubTasks);
		logger.info("The activity task list has been fetched:{}", activiyTasks);
		logger.info("The activity subtask list has been fetched:{}", activiySubTasks);
		return "activity";
	}
}
