package com.pennant.prodmtr.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pennant.prodmtr.model.Dto.ProjectDto;
import com.pennant.prodmtr.model.Dto.UserDto;
import com.pennant.prodmtr.model.Entity.Role;
import com.pennant.prodmtr.model.Entity.User;
import com.pennant.prodmtr.service.Interface.ProjectService;
import com.pennant.prodmtr.service.Interface.ProjectTaskService;
import com.pennant.prodmtr.service.Interface.ResourceService;
import com.pennant.prodmtr.service.Interface.RoleService;
import com.pennant.prodmtr.service.Interface.TaskService;

@Controller
public class PerformanceController {
	private final ResourceService resourceService;
	private final ProjectService projectService;
	private final RoleService roleService;
	private final TaskService taskService;
	private final ProjectTaskService projectTaskService;

	private static final Logger logger = LoggerFactory.getLogger(PerformanceController.class);
	@Autowired
	private User user;

	@Autowired
	private Role role;

	@Autowired
	public PerformanceController(ResourceService resourceService, ProjectService projectService,
			RoleService roleService, TaskService taskService, ProjectTaskService projectTaskService) {
		this.resourceService = resourceService;
		this.projectService = projectService;
		this.roleService = roleService;
		this.taskService = taskService;
		this.projectTaskService = projectTaskService;
		logger.info("The autowiring has been done initially to all the service layers");
	}

	@RequestMapping(value = "/performance", method = RequestMethod.GET)
	public String getAllPerformance(Model model) {
		List<UserDto> resources = resourceService.getAllResources();
		logger.info("Userdto list has been created");
		List<ProjectDto> projects = projectService.getAllProjects(); // Retrieve all projects
		logger.info("project dto has been created to retrieve all the projects");
		List<Role> roles = roleService.getAllRoles(); // Retrieve all roles
		logger.info("Role list has been created to retrieve all the roles");

		// Calculate performance score, hours worked, and tasks completed for each resource
		for (UserDto resource : resources) {
			int completedTasks = taskService.getCompletedTasksByUserId(resource.getUserId());
			logger.info("The completed Tasks are:{}", taskService.getCompletedTasksByUserId(resource.getUserId()));
			int totalTasks = taskService.getTotalTasksByUserId(resource.getUserId());
			logger.info("Total tasks count has been displayed here");
			logger.info("resource name is ", resource.getUserDisplayName());
			int completedProjectTasks = projectTaskService.getCompletedTasksByUserId((long) resource.getUserId());
			logger.info("The completedproject tasks count has been displayed");
			int totalProjectTasks = projectTaskService.getTotalTasksByUserId((long) resource.getUserId());
			logger.info("The totalproject tasks count has been displayed");

			completedTasks = completedTasks + completedProjectTasks;
			totalProjectTasks = totalProjectTasks + totalProjectTasks;
			double performanceScore = taskService.calculatePerformanceScore(completedTasks, totalTasks);

			logger.info("performance score is:{} ", performanceScore);
			// Retrieve hours worked and tasks completed
			double hoursWorked = taskService.getHoursWorkedByUserId(resource.getUserId());

			resource.setPerformanceScore(performanceScore);
			logger.info("Performance score is set");
			resource.setHoursWorked(hoursWorked);
			logger.info("Hours worked has been displayed");
			resource.setTasksCompleted(completedTasks);
			logger.info("completed tasks has been displayed");
			logger.info("total tasks:{} ", totalTasks);
			resource.setTotalTasks(totalProjectTasks);

			model.addAttribute("resources", resources);
			logger.info("Finally the model attribute has been set");

		}
		return "Performance";
	}
}