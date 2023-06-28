package com.pennant.prodmtr.Dao.Impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

import com.pennant.prodmtr.Dao.Interface.RoleDao;
import com.pennant.prodmtr.model.Entity.Role;

@Component
public class RoleDaoImpl implements RoleDao {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Retrieves a list of all roles.
	 *
	 * @return the list of roles
	 */
	public List<Role> getAllRoles() {
		return entityManager.createQuery("SELECT r FROM Role r", Role.class).getResultList();
	}

	/**
	 * Finds a role by ID.
	 *
	 * @param id the ID of the role
	 * @return the role entity
	 */
	public Role findById(short id) {
		return entityManager.find(Role.class, id);
	}
}
