package com.pennant.prodmtr.Dao.Impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.pennant.prodmtr.Dao.Interface.UserDao;
import com.pennant.prodmtr.model.Entity.Subtask;
import com.pennant.prodmtr.model.Entity.Task;
import com.pennant.prodmtr.model.Entity.User;

public class UserDaoImpl implements UserDao {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Updates the password for a user with the given ID.
	 *
	 * @param id       the ID of the user
	 * @param password the new password
	 */
	@Override
	public void UpdatePassword(Integer id, String password) {
		TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class);
		query.setParameter("id", id);

		User user = query.getSingleResult();
		if (user != null) {
			user.setUserPassword(password);
		}
	}

	/**
	 * Retrieves a list of tasks for a given task status.
	 *
	 * @param taskStatus the status of the tasks to retrieve
	 * @return the list of tasks
	 */
	@Override
	public List<Task> getUserActivities(String taskStatus) {
		TypedQuery<Task> query = entityManager.createQuery("SELECT t FROM Task t WHERE t.taskStatus = :taskStatus",
				Task.class);
		query.setParameter("taskStatus", taskStatus);

		List<Task> tasks = query.getResultList();
		return tasks;
	}

	/**
	 * Retrieves a list of subtasks for a given subtask status.
	 *
	 * @param sbts_status the status of the subtasks to retrieve
	 * @return the list of subtasks
	 */
	@Override
	public List<Subtask> getUserSubtaskActivities(String sbts_status) {
		System.out.println("here " + sbts_status);
		TypedQuery<Subtask> query = entityManager
				.createQuery("SELECT s FROM Subtask s WHERE s.sbts_status = :sbts_status", Subtask.class);

		query.setParameter("sbts_status", sbts_status);

		List<Subtask> subtasks = query.getResultList();

		return subtasks;
	}

	/**
	 * Verifies a user by their display name.
	 *
	 * @param userDisplayName the display name of the user
	 * @return the User object if found, null otherwise
	 */
	@Override
	public User verifyUser(String userDisplayName) {
		TypedQuery<User> query = null;

		try {
			query = entityManager.createQuery("SELECT u FROM User u WHERE u.userDisplayName = :userDisplayName",
					User.class);
			query.setParameter("userDisplayName", userDisplayName);
			query.setMaxResults(1);
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}