package edu.leicester.co2103.repo;
import edu.leicester.co2103.domain.Module;

//Helper class to allow for refreshing items when updating.
public interface ModuleRepositoryCustom {
	void refresh(Module m);

}
