package co2103.hw2.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import co2103.hw2.model.Movie;
import co2103.hw2.model.Person;
import co2103.hw2.model.Song;
import co2103.hw2.model.Soundtrack;
import co2103.hw2.repo.MovieRepository;
import co2103.hw2.repo.PersonRepository;
import co2103.hw2.repo.SongRepository;
import co2103.hw2.repo.SoundtrackRepository;

@Controller
public class DeleteController {
	
	@Autowired
	MovieRepository mRepo;
	
	@Autowired
	PersonRepository pRepo;
		
	@Autowired
	SoundtrackRepository soundRepo;
	
	@Autowired
	SongRepository songRepo;
	

	@RequestMapping(value="/deleteMovie", method=RequestMethod.GET)
	public String deleteMovie(@RequestParam int id) {
		Movie movie = mRepo.findById(id);
		mRepo.delete(movie);
		return "redirect:/list";
	}
	
	@RequestMapping(value="/deletePerson", method=RequestMethod.GET)
	public String deletePerson(@RequestParam int id) {
		
		Person person=pRepo.findById(id);
		if(person.getJobTitle().equals("Director")) {
			List<Movie> m=mRepo.findByDirectorId(id);
				if(!m.isEmpty()) {
					for (Movie n:m) {
						n.setDirector(null);
					}
				}
		}
		else if (person.getJobTitle().equals("Composer")) {
			List<Song> songs=songRepo.findByComposerId(id);
			if(!songs.isEmpty()) {
				for (Song s: songs) {
					s.setComposer(null);
				}
			}
		}
		
		pRepo.delete(person);
		
		return "redirect:/list";
	}
	
	@RequestMapping(value="/deleteSoundtrack", method=RequestMethod.GET)
	public String deleteSoundtrack(@RequestParam int id) {
		Soundtrack soundtrack = soundRepo.findById(id);
		mRepo.findBySoundtrackId(id).setSoundtrack(null);
		soundRepo.delete(soundtrack);
		return "redirect:/list";
	}
	
	@RequestMapping(value="/deleteSong", method=RequestMethod.GET)
	public String deleteSong(@RequestParam int id) {
		Optional<Song> song = songRepo.findById(id);
		if(song.isPresent()) {
			songRepo.delete(song.get());
		}
		
		return "redirect:/list";
	}

}
