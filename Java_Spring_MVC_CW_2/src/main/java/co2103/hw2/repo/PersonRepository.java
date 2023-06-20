package co2103.hw2.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import co2103.hw2.model.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {
	
	public Person findById(int id);
	public Person findByFirstName(String firstName);
	public List<Person> findAll();
}
