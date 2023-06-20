package edu.leicester.co2103.repo;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import edu.leicester.co2103.domain.Convenor;

public interface ConvenorRepository extends CrudRepository<Convenor, Long>, ConvenorRepositoryCustom {
	
	Set<Convenor> findAll();
	Optional<Convenor> findById(Long id);
	Set<Convenor> findByModulesCode(String code);

}
