package com.pennant.prodmtr.Dao.Impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pennant.prodmtr.Dao.Interface.SprintDao;
import com.pennant.prodmtr.model.Dto.ModuleDTO;
import com.pennant.prodmtr.model.Entity.FunctionalUnit;
import com.pennant.prodmtr.model.Entity.Sprint;
import com.pennant.prodmtr.model.Entity.SprintResource;
import com.pennant.prodmtr.model.Entity.SprintTasks;
import com.pennant.prodmtr.model.Entity.Task;
import com.pennant.prodmtr.model.Entity.User;

@Repository
@Transactional
public class SprintDaoImpl implements SprintDao {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Retrieves the backlogs, which are sprints containing tasks that have not been completed.
	 *
	 * @return a list of Sprint objects representing the backlogs
	 */
	@Override
	public List<Sprint> getBaskLogs() {
		String query = "SELECT s FROM Sprint s WHERE EXISTS (SELECT 1 FROM Task t WHERE t.module.id = s.moduleId.id AND t.taskCompletedDateTime IS NULL)";
		return entityManager.createQuery(query, Sprint.class).getResultList();
	}

	/**
	 * Retrieves the details of a specific sprint.
	 *
	 * @param sprintId the ID of the sprint
	 * @return the Sprint object if found, null otherwise
	 */
	@Override
	public Sprint getSprintDetails(int sprintId) {
		return entityManager.find(Sprint.class, sprintId);
	}

	/**
	 * Retrieves the tasks associated with a specific module.
	 *
	 * @param modlId the ID of the module
	 * @return a list of Task objects associated with the module
	 */
	@Override
	public List<Task> getTasks(int modlId) {
		String query = "SELECT t FROM Task t WHERE t.module.id = :modlId";
		return entityManager.createQuery(query, Task.class).setParameter("modlId", modlId).getResultList();
	}

	/**
	 * Retrieves all sprints.
	 *
	 * @return a list of all Sprint objects
	 */
	@Override
	public List<Sprint> getAllSprints() {
		String query = "SELECT s FROM Sprint s";
		return entityManager.createQuery(query, Sprint.class).getResultList();
	}

	/**
	 * Retrieves all tasks associated with a specific sprint.
	 *
	 * @param sprintId the ID of the sprint
	 * @return a list of SprintTasks objects representing the tasks associated with the sprint
	 */
	@Override
	public List<SprintTasks> getAllTasksBySprintId(Sprint sprintId) {
		String query = "SELECT st FROM SprintTasks st WHERE st.id.sprnId = :sprintId";
		return entityManager.createQuery(query, SprintTasks.class).setParameter("sprintId", sprintId).getResultList();
	}

	/**
	 * Stores a sprint in the database.
	 *
	 * @param sprint the Sprint object to be stored
	 * @return the stored Sprint object
	 */
	@Override
	public Sprint storeSprint(Sprint sprint) {
		if (sprint.getSprintId() == 0) {
			entityManager.persist(sprint); // New entity, use persist
		} else {
			entityManager.merge(sprint); // Existing entity, use merge
		}
		return sprint;
	}

	/**
	 * Retrieves the modules associated with a specific project for creating sprints.
	 *
	 * @param projectId the ID of the project
	 * @return a list of ModuleDTO objects representing the modules available for sprints
	 */
	@Override
	public List<ModuleDTO> getSprintModulesByProjectId(int projectId) {
		String query = "SELECT new com.nkxgen.spring.orm.model.ModuleDTO(m.id, m.name, m.description, m.project.projectId) FROM Module m WHERE m.project.projectId = :projectId AND m.id NOT IN (SELECT s.moduleId.id FROM Sprint s)";
		TypedQuery<ModuleDTO> typedQuery = entityManager.createQuery(query, ModuleDTO.class);
		typedQuery.setParameter("projectId", projectId);
		List<ModuleDTO> moduleDTOList = typedQuery.getResultList();
		return moduleDTOList;
	}

	/**
	 * Retrieves the functional units associated with a specific module.
	 *
	 * @param modl_id the ID of the module
	 * @return a list of FunctionalUnit objects associated with the module
	 */
	@Override
	public List<FunctionalUnit> getFunctionalUnitsByModId(int modl_id) {
		String query = "SELECT distinct fu FROM FunctionalUnit fu WHERE fu.id.module.id = :modlId";
		return entityManager.createQuery(query, FunctionalUnit.class).setParameter("modlId", modl_id).getResultList();
	}

	/**
	 * Stores a task in the database.
	 *
	 * @param task the Task object to be stored
	 * @return the stored Task object
	 */
	@Override
	public Task storeTask(Task task) {
		if (task.getTaskId() == 0) {
			entityManager.persist(task); // New entity, use persist
		} else {
			entityManager.merge(task); // Existing entity, use merge
		}
		return task;
	}

	/**
	 * Retrieves all users.
	 *
	 * @return a list of all User objects
	 */
	@Override
	public List<User> getAllUsers() {
		String query = "SELECT u FROM User u";
		return entityManager.createQuery(query, User.class).getResultList();
	}

	/**
	 * Stores a sprint resource in the database.
	 *
	 * @param src the SprintResource object to be stored
	 */
	@Override
	public void storeSprintResource(SprintResource src) {
		entityManager.persist(src);
	}

	/**
	 * Stores a sprint task in the database.
	 *
	 * @param sprintTask the SprintTasks object to be stored
	 */
	@Override
	public void storeSprintTasks(SprintTasks sprintTask) {
		entityManager.persist(sprintTask);
	}

	/**
	 * Retrieves the sprints associated with a specific project.
	 *
	 * @param projId the ID of the project
	 * @return a list of Sprint objects associated with the project
	 */
	@Override
	public List<Sprint> getSprintByProjId(int projId) {
		TypedQuery<Sprint> query = entityManager
				.createQuery("SELECT s FROM Sprint s WHERE s.projectId.projectId = :projId", Sprint.class);
		query.setParameter("projId", (short) projId);
		List<Sprint> sprints = query.getResultList();
		return sprints;
	}
}