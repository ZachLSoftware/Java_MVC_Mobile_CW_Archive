package edu.leicester.co2103.repo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import edu.leicester.co2103.domain.Module;

//Refresh implementation
public class ModuleRepositoryImpl implements ModuleRepositoryCustom {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	@Transactional
	public void refresh(Module m) {
		em.refresh(m);
		
	}

}
