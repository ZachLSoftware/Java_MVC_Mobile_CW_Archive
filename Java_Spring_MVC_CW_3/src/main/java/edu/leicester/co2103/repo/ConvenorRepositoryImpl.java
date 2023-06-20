package edu.leicester.co2103.repo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import edu.leicester.co2103.domain.Convenor;


//Implements a refresh functionality
public class ConvenorRepositoryImpl implements ConvenorRepositoryCustom {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	@Transactional
	public void refresh(Convenor c) {
		em.refresh(c);
	}

}
