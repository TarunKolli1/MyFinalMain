package com.pennant.prodmtr.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.pennant.prodmtr.model.Dto.ProjectDto;
import com.pennant.prodmtr.model.Dto.UserDto;
import com.pennant.prodmtr.model.Entity.Role;
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

	public PerformanceController(ResourceService resourceService, ProjectService projectService,
			RoleService roleService, TaskService taskService, ProjectTaskService projectTaskService) {
		this.resourceService = resourceService;
		this.projectService = projectService;
		this.roleService = roleService;
		this.taskService = taskService;
		this.projectTaskService = projectTaskService;
	}

	/**
	 * Retrieves performance details for all resources and displays the performance page.
	 *
	 * @param model the model to be populated with data
	 * @return the view name for the performance page
	 */
	@GetMapping("/performance")
	public String getAllPerformance(Model model) {
		List<UserDto> resources = resourceService.getAllResources();
		List<ProjectDto> projects = projectService.getAllProjects();
		List<Role> roles = roleService.getAllRoles();

		for (UserDto resource : resources) {
			int completedTasks = taskService.getCompletedTasksByUserId(resource.getUserId());
			int totalTasks = taskService.getTotalTasksByUserId(resource.getUserId());
			int completedProjectTasks = projectTaskService.getCompletedTasksByUserId((long) resource.getUserId());
			int totalProjectTasks = projectTaskService.getTotalTasksByUserId((long) resource.getUserId());

			completedTasks += completedProjectTasks;
			totalTasks += totalProjectTasks;
			double performanceScore = taskService.calculatePerformanceScore(completedTasks, totalTasks);

			double hoursWorked = taskService.getHoursWorkedByUserId(resource.getUserId());

			resource.setPerformanceScore(performanceScore);
			resource.setHoursWorked(hoursWorked);
			resource.setTasksCompleted(completedTasks);
			resource.setTotalTasks(totalTasks);
		}

		model.addAttribute("resources", resources);
		model.addAttribute("projects", projects);
		model.addAttribute("roles", roles);

		return "Performance";
	}
}
