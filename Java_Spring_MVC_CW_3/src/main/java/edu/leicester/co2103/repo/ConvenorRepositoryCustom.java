package edu.leicester.co2103.repo;

import edu.leicester.co2103.domain.Convenor;

//Helper interface used to refresh repository when updating items
public interface ConvenorRepositoryCustom {
	void refresh(Convenor c);

}
