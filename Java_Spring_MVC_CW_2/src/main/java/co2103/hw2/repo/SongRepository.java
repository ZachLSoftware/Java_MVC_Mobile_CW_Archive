package co2103.hw2.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import co2103.hw2.model.Song;


public interface SongRepository extends CrudRepository<Song, Integer> {
	
	public List<Song> findAll();
	
	public List<Song> findByComposerId(int id);
	
	public Optional<Song> findById(int id);

}
