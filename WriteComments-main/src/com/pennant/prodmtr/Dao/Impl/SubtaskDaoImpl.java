package com.pennant.prodmtr.Dao.Impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.pennant.prodmtr.Dao.Interface.SubtaskDao;
import com.pennant.prodmtr.model.Entity.Subtask;

@Repository
public class SubtaskDaoImpl implements SubtaskDao {

	@PersistenceContext
	private EntityManager entityManager;
	private Object subtaskId;

	/**
	 * Saves a subtask in the database.
	 *
	 * @param subtask the Subtask object to be saved
	 * @return the saved Subtask object
	 */
	public Subtask save(Subtask subtask) {
		System.out.println(subtask);
		entityManager.persist(subtask);
		return subtask;
	}

	/**
	 * Saves a subtask in the database.
	 *
	 * @param subtask the Subtask object to be saved
	 */
	public void saveSubtask(Subtask subtask) {
		entityManager.persist(subtask);
	}

	/**
	 * Sets a new subtask in the database.
	 *
	 * @param subtask the Subtask object to be set
	 */
	public void setNewSubTask(Subtask subtask) {
		entityManager.persist(subtask);
	}

	/**
	 * Retrieves the subtask ID.
	 *
	 * @return the subtask ID
	 */
	public Object getSubtaskId() {
		return subtaskId;
	}

	/**
	 * Sets the subtask ID.
	 *
	 * @param subtaskId the subtask ID to be set
	 */
	public void setSubtaskId(Object subtaskId) {
		this.subtaskId = subtaskId;
	}
}