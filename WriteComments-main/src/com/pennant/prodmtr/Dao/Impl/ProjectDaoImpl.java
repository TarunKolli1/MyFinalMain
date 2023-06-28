package com.pennant.prodmtr.Dao.Impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.pennant.prodmtr.Dao.Interface.ProjectDao;
import com.pennant.prodmtr.model.Dto.ProjectDto;
import com.pennant.prodmtr.model.Dto.ProjectFilter;
import com.pennant.prodmtr.model.Entity.Project;

@Component
public class ProjectDaoImpl implements ProjectDao {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Retrieves all projects.
	 *
	 * @return the list of project DTOs
	 */
	public List<ProjectDto> getAllProjects() {
		TypedQuery<Project> query = entityManager.createQuery("SELECT pt FROM Project pt", Project.class);
		List<Project> projects = query.getResultList();
		List<ProjectDto> convertedProjects = projects.stream().map(ProjectDto::fromEntity).collect(Collectors.toList());
		return convertedProjects;
	}

	/**
	 * Finds a project by ID.
	 *
	 * @param id the ID of the project
	 * @return the project entity
	 */
	public Project findById(short id) {
		return entityManager.find(Project.class, id);
	}

	/**
	 * Retrieves filtered projects based on the project filter.
	 *
	 * @param projectFilter the project filter
	 * @return the list of filtered project DTOs
	 */
	public List<ProjectDto> getFilterProjects(ProjectFilter projectFilter) {
		String jpql = "SELECT p FROM Project p WHERE 1 = 1";
		TypedQuery<Project> query = entityManager.createQuery(jpql, Project.class);

		Short projectId = projectFilter.getProjectId();
		String status = projectFilter.getStatus();

		if (projectId != null) {
			jpql += " AND p.projectId = :projectId";
			query.setParameter("projectId", projectId);
		}
		if (status != null && !status.isEmpty()) {
			jpql += " AND p.status = :status";
			query.setParameter("status", status);
		}

		List<Project> projects = query.getResultList();
		List<ProjectDto> convertedProjects = projects.stream().map(ProjectDto::fromEntity).collect(Collectors.toList());
		return convertedProjects;
	}

	/**
	 * Retrieves a project by ID.
	 *
	 * @param proj_id the ID of the project
	 * @return the project DTO if found, or null if not found
	 */
	public ProjectDto getProjectById(Integer proj_id) {
		short getProjectId = proj_id.shortValue();
		TypedQuery<Project> query = entityManager.createQuery("SELECT pt FROM Project pt WHERE pt.projectId = :proj_id",
				Project.class);
		query.setParameter("proj_id", getProjectId);

		List<Project> projects = query.getResultList();

		if (!projects.isEmpty()) {
			Project project = projects.get(0);
			return ProjectDto.fromEntity(project);
		} else {
			return null;
		}
	}

	/**
	 * Saves a new project.
	 *
	 * @param project the project to be saved
	 */
	public void setNewProject(Project project) {
		entityManager.persist(project);
	}
}
