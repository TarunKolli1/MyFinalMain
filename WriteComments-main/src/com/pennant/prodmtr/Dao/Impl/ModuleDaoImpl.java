package com.pennant.prodmtr.Dao.Impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.pennant.prodmtr.Dao.Interface.ModuleDao;
import com.pennant.prodmtr.model.Dto.ModuleDTO;
import com.pennant.prodmtr.model.Entity.Module;

@Component
public class ModuleDaoImpl implements ModuleDao {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Retrieves module details by project ID using ORM.
	 *
	 * @param projId the ID of the project
	 * @return the list of module DTOs
	 */
	public List<ModuleDTO> getModuleDetailsByProjId(Integer projId) {
		short getProjectId = projId.shortValue();
		TypedQuery<Module> query = entityManager
				.createQuery("SELECT pt FROM Module pt WHERE pt.moduleProject.projectId = :proj_id", Module.class);
		query.setParameter("proj_id", getProjectId);

		List<Module> modules = query.getResultList();
		List<ModuleDTO> moduleDTOList = new ArrayList<>();

		for (Module module : modules) {
			ModuleDTO moduleDTO = ModuleDTO.fromEntity(module);
			moduleDTOList.add(moduleDTO);
		}

		return moduleDTOList;
	}

	/**
	 * Saves a module in a project using ORM.
	 *
	 * @param module the module to be saved
	 */
	public void save(Module module) {
		System.out.println(module);
		entityManager.persist(module);
	}
}
