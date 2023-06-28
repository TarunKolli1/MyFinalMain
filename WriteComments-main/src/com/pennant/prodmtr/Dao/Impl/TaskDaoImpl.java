package com.pennant.prodmtr.Dao.Impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.pennant.prodmtr.Dao.Interface.TaskDao;
import com.pennant.prodmtr.model.Dto.ResTaskFilter;
import com.pennant.prodmtr.model.Dto.TaskDto;
import com.pennant.prodmtr.model.Entity.Project;
import com.pennant.prodmtr.model.Entity.Task;
import com.pennant.prodmtr.model.Entity.User;

@Repository
@Component
public class TaskDaoImpl implements TaskDao {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Retrieves the list of tasks for a given user.
	 *
	 * @param userId the ID of the user
	 * @return the list of TaskDto objects for the user
	 */
	public List<TaskDto> viewTasksForUser(int userId) {
		return getTasksByUserId(userId);
	}

	/**
	 * Finds a project by its ID.
	 *
	 * @param projectId the ID of the project
	 * @return the Project object
	 */
	public Project findProjectById(Short projectId) {
		System.out.println("in findprojbyid");
		return entityManager.find(Project.class, projectId);
	}

	/**
	 * Finds a user by their ID.
	 *
	 * @param userId the ID of the user
	 * @return the User object
	 */
	private User findById(int userId) {
		return entityManager.find(User.class, userId);
	}

	/**
	 * Retrieves the list of tasks for a given user.
	 *
	 * @param userId the ID of the user
	 * @return the list of TaskDto objects for the user
	 */
	public List<TaskDto> getTasksByUserId(int userId) {
		User user = findById(userId);
		String jpql = "SELECT t FROM Task t WHERE t.taskSupervisor = :user";
		TypedQuery<Task> taskQuery = entityManager.createQuery(jpql, Task.class);
		taskQuery.setParameter("user", user);
		List<Task> tasks = taskQuery.getResultList();

		List<TaskDto> taskDTOs = new ArrayList<>();
		for (Task task : tasks) {
			TaskDto dto = TaskDto.fromEntity(task);
			taskDTOs.add(dto);
		}

		return taskDTOs;
	}

	/**
	 * Retrieves all tasks.
	 *
	 * @return the list of all TaskDto objects
	 */
	public List<TaskDto> getAllTasks() {
		String jpql = "SELECT t FROM Task t";// query
		TypedQuery<Task> taskQuery = entityManager.createQuery(jpql, Task.class);
		List<Task> tasks = taskQuery.getResultList();

		List<TaskDto> taskDTOs = new ArrayList<>();
		for (Task task : tasks) {
			TaskDto dto = TaskDto.fromEntity(task);
			taskDTOs.add(dto);
		}

		return taskDTOs;
	}

	/**
	 * Filters tasks based on the provided filter parameters.
	 *
	 * @param resTaskFilter the filter parameters
	 * @return the list of filtered TaskDto objects
	 */
	public List<TaskDto> filterTasks(ResTaskFilter resTaskFilter) {
		System.out.println("In filterTasks");

		String jpql = "SELECT t FROM Task t WHERE 1 = 1 AND t.taskSupervisor.id = :userId";

		TypedQuery<Task> query = entityManager.createQuery(jpql, Task.class);

		query.setParameter("userId", resTaskFilter.getUserId());

		if (resTaskFilter.getProject() != null && !resTaskFilter.getStatus().isEmpty()
				&& !resTaskFilter.getCategory().isEmpty()) {
			jpql = "SELECT t FROM Task t WHERE 1 = 1 AND t.taskSupervisor.id = :userId AND t.taskCategory = :category AND t.taskStatus = :status AND t.project.id = :projectId";
			findProjectById(resTaskFilter.getProject());
			query.setParameter("projectId", resTaskFilter.getProject());
		} else {
			if (resTaskFilter.getProject() != null && resTaskFilter.getProject() != 0) {
				findProjectById(resTaskFilter.getProject());
				query.setParameter("projectId", resTaskFilter.getProject());
			}

			if (resTaskFilter.getStatus() != null && !resTaskFilter.getStatus().isEmpty()) {
				query.setParameter("status", resTaskFilter.getStatus());
			}

			if (resTaskFilter.getCategory() != null && !resTaskFilter.getCategory().isEmpty()) {
				query.setParameter("category", resTaskFilter.getCategory());
			}
		}

		List<Task> filteredTasks = query.getResultList();
		List<TaskDto> filteredTaskDTOs = new ArrayList<>();

		for (Task task : filteredTasks) {
			TaskDto dto = TaskDto.fromEntity(task);
			filteredTaskDTOs.add(dto);
		}

		return filteredTaskDTOs;
	}

	/**
	 * Retrieves a task by its ID.
	 *
	 * @param taskId the ID of the task
	 * @return the Task object
	 */
	public Task getTaskById(int taskId) {
		return entityManager.find(Task.class, taskId);
	}

	/**
	 * Saves a task in the database.
	 *
	 * @param task the Task object to be saved
	 */
	public void saveTask(Task task) {
		entityManager.merge(task);
	}

	/**
	 * Updates the status of a task.
	 *
	 * @param taskId the ID of the task to be updated
	 * @return true if the status update is successful, false otherwise
	 */
	public boolean updateStatus(int taskId) {
		String jpql = "UPDATE Task t SET t.taskStatus = :status WHERE t.taskId = :taskId";
		Query query = entityManager.createQuery(jpql);
		query.setParameter("status", "rew");
		query.setParameter("taskId", taskId);

		int rowsAffected = query.executeUpdate();
		return rowsAffected > 0;
	}

	/**
	 * Retrieves the list of tasks for a given project ID.
	 *
	 * @param projId the ID of the project
	 * @return the list of Task objects for the project
	 */
	public List<Task> getTasksByProjectId(int projId) {
		short projectId = (short) projId;
		TypedQuery<Task> query = entityManager
				.createQuery("SELECT pt FROM Task pt WHERE pt.project.projectId = :projectId", Task.class);
		query.setParameter("projectId", projectId);
		return query.getResultList();
	}

	/**
	 * Retrieves the list of completed tasks for a given project ID.
	 *
	 * @param projId the ID of the project
	 * @return the list of completed Task objects for the project
	 */
	public List<Task> getCompTasksByProjectId(int projId) {
		short projectId = (short) projId;
		TypedQuery<Task> query = entityManager.createQuery(
				"SELECT pt FROM Task pt WHERE pt.project.projectId = :projectId and pt.taskStatus = 'DONE' ",
				Task.class);
		query.setParameter("projectId", projectId);
		return query.getResultList();
	}

	/**
	 * Retrieves the number of completed tasks for a given user ID.
	 *
	 * @param userId the ID of the user
	 * @return the number of completed tasks
	 */
	public int getCompletedTasksByUserId(int userId) {
		User user = findById(userId);
		String jpql = "SELECT COUNT(t) FROM Task t WHERE t.taskSupervisor = :user AND t.taskStatus = 'completed'";
		TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
		query.setParameter("user", user);
		Long result = query.getSingleResult();
		return result != null ? result.intValue() : 0;
	}

	/**
	 * Retrieves the total number of tasks for a given user ID.
	 *
	 * @param userId the ID of the user
	 * @return the total number of tasks
	 */
	public int getTotalTasksByUserId(int userId) {
		User user = findById(userId);
		String jpql = "SELECT COUNT(t) FROM Task t WHERE t.taskSupervisor = :user";
		TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
		query.setParameter("user", user);
		Long result = query.getSingleResult();
		return result != null ? result.intValue() : 0;
	}

	/**
	 * Retrieves the total number of hours worked by a given user ID.
	 *
	 * @param userId the ID of the user
	 * @return the total number of hours worked
	 */
	public double getHoursWorkedByUserId(int userId) {
		User user = findById(userId);
		String jpql = "SELECT SUM(t.numberOfHoursRequired) FROM Task t WHERE t.taskSupervisor = :user";
		TypedQuery<BigDecimal> query = entityManager.createQuery(jpql, BigDecimal.class);
		query.setParameter("user", user);
		BigDecimal result = query.getSingleResult();
		return result != null ? result.doubleValue() : 0;
	}

	/**
	 * Updates the remarks and status of a task.
	 *
	 * @param taskId  the ID of the task to be updated
	 * @param remarks the updated remarks
	 * @param status  the updated status
	 */
	@Override
	public void updateEntity(int taskId, String remarks, String status) {
		System.out.println("in updateEntity");
		Task task = entityManager.find(Task.class, taskId);
		System.out.println("entity " + task);
		task.setTaskRemarks(remarks);
		task.setTaskStatus(status);
		entityManager.merge(task);
		entityManager.flush();
	}

	/**
	 * Sets the remarks and status of a task.
	 *
	 * @param taskId      the ID of the task to be updated
	 * @param taskRemarks the task remarks
	 * @param taskStatus  the task status
	 */
	@Override
	public void setTaskStatus(int taskId, String taskRemarks, String taskStatus) {
		Task task = entityManager.find(Task.class, taskId);
		task.setTaskRemarks(taskRemarks);
		task.setTaskStatus(taskStatus);
		entityManager.persist(task);
	}
}