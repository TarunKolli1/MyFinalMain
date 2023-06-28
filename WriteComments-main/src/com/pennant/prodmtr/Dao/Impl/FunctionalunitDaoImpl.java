package com.pennant.prodmtr.Dao.Impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.pennant.prodmtr.Dao.Interface.FunctionalunitDao;
import com.pennant.prodmtr.model.Dto.FunctionalUnitdto;
import com.pennant.prodmtr.model.Entity.FunctionalUnit;

@Component
public class FunctionalunitDaoImpl implements FunctionalunitDao {

	public FunctionalunitDaoImpl() {
	}

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Retrieves functional units by module ID using Spring ORM.
	 *
	 * @param modId the ID of the module
	 * @return the list of functional units
	 */
	public List<FunctionalUnitdto> getFunctionalunitByModId(int modId) {
		TypedQuery<FunctionalUnit> query = entityManager
				.createQuery("SELECT pt FROM FunctionalUnit pt WHERE pt.id.module.id = :modl_id", FunctionalUnit.class);
		query.setParameter("modl_id", (short) modId);
		List<FunctionalUnit> funit = query.getResultList();
		List<FunctionalUnitdto> funitlist = new ArrayList<>();

		for (FunctionalUnit functionunit : funit) {
			FunctionalUnitdto funitDTO = FunctionalUnitdto.fromEntity(functionunit);
			funitlist.add(funitDTO);
		}
		return funitlist;
	}

	/**
	 * Saves a functional unit using ORM.
	 *
	 * @param funit the functional unit to be saved
	 */
	public void save(FunctionalUnit funit) {
		System.out.println(funit);
		entityManager.persist(funit);
	}
}
