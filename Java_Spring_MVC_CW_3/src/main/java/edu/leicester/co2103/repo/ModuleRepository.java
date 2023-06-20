package edu.leicester.co2103.repo;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import edu.leicester.co2103.domain.Module;

public interface ModuleRepository extends CrudRepository<Module, String>, ModuleRepositoryCustom {
	Optional<Module> findById(String code);
	Set<Module> findAll();

}
