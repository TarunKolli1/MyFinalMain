package com.pennant.prodmtr.Dao.Impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.pennant.prodmtr.Dao.Interface.SubtaskDao;
import com.pennant.prodmtr.model.Entity.Subtask;

@Repository
public class SubtaskDaoImpl implements SubtaskDao {

	private static final Logger logger = LoggerFactory.getLogger(SubtaskDaoImpl.class);
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
		logger.info("The subtask is:{}", subtask);
		entityManager.persist(subtask);
		logger.info("Subtask is returned");
		return subtask;
	}

	/**
	 * Saves a subtask in the database.
	 *
	 * @param subtask the Subtask object to be saved
	 */
	public void saveSubtask(Subtask subtask) {
		entityManager.persist(subtask);
		logger.info("subtask has been persisted and it is saved");
	}

	/**
	 * Sets a new subtask in the database.
	 *
	 * @param subtask the Subtask object to be set
	 */
	public void setNewSubTask(Subtask subtask) {
		System.out.println(subtask + "subtask ");
		entityManager.persist(subtask);
		logger.info("subtask has been persisted and it is created");
	}

	/**
	 * Retrieves the subtask ID.
	 *
	 * @return the subtask ID
	 */
	public Object getSubtaskId() {
		logger.info("The subtask id is returned");
		return subtaskId;
	}

	/**
	 * Sets the subtask ID.
	 *
	 * @param subtaskId the subtask ID to be set
	 */
	public void setSubtaskId(Object subtaskId) {
		this.subtaskId = subtaskId;
		logger.info("The subtask id assigned is:{}", subtaskId);
	}
}