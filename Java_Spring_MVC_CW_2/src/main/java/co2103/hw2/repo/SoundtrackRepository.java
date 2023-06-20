package co2103.hw2.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import co2103.hw2.model.Soundtrack;

public interface SoundtrackRepository extends CrudRepository<Soundtrack, String> {
	
	public List<Soundtrack> findAll();

	public Soundtrack findById(int soundtrackId);

}
