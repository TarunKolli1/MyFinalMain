package com.pennant.prodmtr.Dao.Impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.pennant.prodmtr.Dao.Interface.ResourceDao;
import com.pennant.prodmtr.model.Dto.UserDto;
import com.pennant.prodmtr.model.Entity.Project;
import com.pennant.prodmtr.model.Entity.Role;
import com.pennant.prodmtr.model.Entity.User;

@Component
public class ResourceDaoImpl implements ResourceDao {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Retrieves a list of all resources.
	 *
	 * @return the list of user DTOs representing resources
	 */
	public List<UserDto> getAllResources() {
		String jpql = "SELECT r FROM User r";
		TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
		List<User> users = query.getResultList();

		List<UserDto> userDtos = users.stream().map(UserDto::fromEntity).collect(Collectors.toList());

		return userDtos;
	}

	/**
	 * Filters resources based on role and project.
	 *
	 * @param roleFilter    the ID of the role to filter by
	 * @param projectFilter the ID of the project to filter by
	 * @return the list of filtered user DTOs
	 */
	public List<UserDto> filterResources(Short roleFilter, Short projectFilter) {
		String jpql = "SELECT DISTINCT u FROM User u WHERE 1=1  AND ( u.userRole.id = :roleFilter AND  u.id IN (SELECT t.taskSupervisor.id FROM Task t WHERE t.project.id = :projectFilter))";
		String projqry = "SELECT DISTINCT u FROM User u WHERE 1=1  AND  u.userRole.id = :roleFilter";
		String roleqry = "SELECT DISTINCT u FROM User u WHERE 1=1  AND u.id IN (SELECT t.taskSupervisor.id FROM Task t WHERE t.project.id = :projectFilter)";
		String allqry = "SELECT DISTINCT u FROM User u";

		if (roleFilter == null) {
			jpql = roleqry;
			if (projectFilter == null) {
				jpql = allqry;
			}
		} else if (projectFilter == null) {
			jpql = projqry;
		}

		TypedQuery<User> query = entityManager.createQuery(jpql, User.class);

		if (roleFilter != null) {
			Role role = findById(roleFilter);
			query.setParameter("roleFilter", role.getRoleId());
		}

		if (projectFilter != null) {
			Project project = findProjectById(projectFilter);
			query.setParameter("projectFilter", project.getProjectId());
		}

		List<User> users = query.getResultList();

		List<UserDto> userDtos = users.stream().map(UserDto::fromEntity).collect(Collectors.toList());

		return userDtos;
	}

	/**
	 * Finds a role by ID.
	 *
	 * @param id the ID of the role
	 * @return the role entity
	 */
	public Role findById(Short id) {
		return entityManager.find(Role.class, id);
	}

	/**
	 * Finds a project by ID.
	 *
	 * @param projectId the ID of the project
	 * @return the project entity
	 */
	public Project findProjectById(Short projectId) {
		return entityManager.find(Project.class, projectId);
	}

	/**
	 * Retrieves a resource by display name.
	 *
	 * @param displayName the display name of the resource
	 * @return the user entity representing the resource
	 */
	public User getResourceByDisplayName(String displayName) {
		String jpql = "SELECT r FROM User r WHERE r.userDisplayName = :displayName";
		TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
		query.setParameter("displayName", displayName);

		List<User> resultList = query.getResultList();
		if (!resultList.isEmpty()) {
			return resultList.get(0);
		}

		return resultList.get(0);
	}

	/**
	 * Finds a user by display name.
	 *
	 * @param displayName the display name of the user
	 * @return the user entity
	 */
	private User findByName(String displayName) {
		return entityManager.find(User.class, displayName);
	}

	/**
	 * Adds a new user.
	 *
	 * @param user the user entity to add
	 */
	public void addUser(User user) {
		entityManager.persist(user);
	}

	/**
	 * Saves an existing user.
	 *
	 * @param existingResource the existing user entity to save
	 */
	public void saveUser(User existingResource) {
		entityManager.merge(existingResource);
	}

	/**
	 * Retrieves a list of all project managers.
	 *
	 * @return the list of project manager users
	 */
	public List<User> getAllProjManagers() {
		String jpql = "SELECT r FROM User r where userRole.roleId = 1 or userRole.roleId = 2";
		Query query = entityManager.createQuery(jpql);
		return query.getResultList();
	}

	/**
	 * Retrieves a list of users associated with a project.
	 *
	 * @param projectId the ID of the project
	 * @return the list of users associated with the project
	 */
	public List<User> getUsersByProjectId(int projectId) {
		short pid = (short) projectId;
		String jpql = "SELECT DISTINCT u FROM Task t JOIN t.taskSupervisor u WHERE t.project.projectId = :projectId";
		TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
		query.setParameter("projectId", pid);
		return query.getResultList();
	}

	/**
	 * Retrieves a resource by ID.
	 *
	 * @param userId the ID of the resource
	 * @return the user entity representing the resource
	 */
	public User getResourceById(int userId) {
		String jpql = "SELECT r FROM User r WHERE r.userId = :userId";
		TypedQuery<User> query = entityManager.createQuery(jpql, User.class);
		query.setParameter("userId", userId);

		List<User> resultList = query.getResultList();
		if (!resultList.isEmpty()) {
			return resultList.get(0);
		}
		return null;
	}

}
