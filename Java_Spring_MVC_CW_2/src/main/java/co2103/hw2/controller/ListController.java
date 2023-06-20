package co2103.hw2.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import co2103.hw2.model.Movie;
import co2103.hw2.model.Person;
import co2103.hw2.model.Song;

import co2103.hw2.model.Soundtrack;
import co2103.hw2.repo.*;

@Controller
public class ListController {
	@Autowired
	MovieRepository mRepo;
	
	@Autowired
	PersonRepository pRepo;
		
	@Autowired
	SoundtrackRepository soundRepo;
	
	@Autowired
	SongRepository songRepo;

	@RequestMapping(value="/list", method=RequestMethod.GET)
	public String listObjects(Model model) {
		
			
			model.addAttribute("movies", mRepo.findAll());
			model.addAttribute("people", pRepo.findAll());
			model.addAttribute("soundtracks", soundRepo.findAll());
			model.addAttribute("songs", songRepo.findAll());
		
		return "list";
		
	}
	
}
