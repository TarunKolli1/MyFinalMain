package com.pennant.prodmtr.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pennant.prodmtr.model.Dto.UserDto;
import com.pennant.prodmtr.model.Entity.Role;
import com.pennant.prodmtr.service.Interface.PerformanceService;
import com.pennant.prodmtr.service.Interface.ProjectService;
import com.pennant.prodmtr.service.Interface.ProjectTaskService;
import com.pennant.prodmtr.service.Interface.ResourceService;
import com.pennant.prodmtr.service.Interface.RoleService;
import com.pennant.prodmtr.service.Interface.TaskService;

@Service
public class PerformanceServiceImpl implements PerformanceService {
	private final ResourceService resourceService;
	private final ProjectService projectService;
	private final RoleService roleService;
	private final TaskService taskService;
	private final ProjectTaskService projectTaskService;

	@Autowired
	public PerformanceServiceImpl(ResourceService resourceService, ProjectService projectService,
			RoleService roleService, TaskService taskService, ProjectTaskService projectTaskService) {
		this.resourceService = resourceService;
		this.projectService = projectService;
		this.roleService = roleService;
		this.taskService = taskService;
		this.projectTaskService = projectTaskService;
	}

	@Override
	public List<UserDto> getAllResources() {
		return resourceService.getAllResources();
	}

	@Override
	public List<Role> getAllRoles() {
		return roleService.getAllRoles();
	}

	@Override
	public int getCompletedTasksByUserId(int userId) {
		return taskService.getCompletedTasksByUserId(userId);
	}

	@Override
	public int getTotalTasksByUserId(int userId) {
		return taskService.getTotalTasksByUserId(userId);
	}

	@Override
	public int getCompletedProjectTasksByUserId(long userId) {
		return projectTaskService.getCompletedTasksByUserId(userId);
	}

	@Override
	public int getTotalProjectTasksByUserId(long userId) {
		return projectTaskService.getTotalTasksByUserId(userId);
	}

	@Override
	public double calculatePerformanceScore(int completedTasks, int totalTasks) {
		return taskService.calculatePerformanceScore(completedTasks, totalTasks);
	}

	@Override
	public double getHoursWorkedByUserId(int userId) {
		return taskService.getHoursWorkedByUserId(userId);
	}
}
