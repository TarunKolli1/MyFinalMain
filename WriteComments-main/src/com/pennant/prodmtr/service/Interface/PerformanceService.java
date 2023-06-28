package com.pennant.prodmtr.service.Interface;

import java.util.List;

import com.pennant.prodmtr.model.Dto.UserDto;
import com.pennant.prodmtr.model.Entity.Role;

public interface PerformanceService {
	List<UserDto> getAllResources();

	List<Role> getAllRoles();

	int getCompletedTasksByUserId(int userId);

	int getTotalTasksByUserId(int userId);

	int getCompletedProjectTasksByUserId(long userId);

	int getTotalProjectTasksByUserId(long userId);

	double calculatePerformanceScore(int completedTasks, int totalTasks);

	double getHoursWorkedByUserId(int userId);
}
