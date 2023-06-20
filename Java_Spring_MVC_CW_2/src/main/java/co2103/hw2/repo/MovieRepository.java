package co2103.hw2.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import co2103.hw2.model.Movie;

public interface MovieRepository extends CrudRepository<Movie, String> {
	
	public Movie findById(int id);
	public Movie findByTitle(String title);
	public List<Movie> findByActorsId(int id);
	public List<Movie> findByDirectorId(int id);
	public List<Movie> findAll();
	public Movie findBySoundtrackId(int id);

}
