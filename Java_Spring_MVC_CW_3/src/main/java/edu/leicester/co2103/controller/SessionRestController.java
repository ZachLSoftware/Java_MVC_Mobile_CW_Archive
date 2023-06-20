package edu.leicester.co2103.controller;


import java.util.Set;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.leicester.co2103.domain.Session;
import edu.leicester.co2103.domain.Convenor;
import edu.leicester.co2103.domain.Reply;
import edu.leicester.co2103.domain.Module;
import edu.leicester.co2103.repo.ConvenorRepository;
import edu.leicester.co2103.repo.ModuleRepository;
import edu.leicester.co2103.repo.SessionRepository;

@RestController
public class SessionRestController {
	@Autowired
	SessionRepository repo;
	
	@Autowired
	ConvenorRepository crepo;
	
	@Autowired
	ModuleRepository mrepo;
	
	//Endpoint 18
	@DeleteMapping("/sessions")
	public ResponseEntity<?> deleteAllSessions(){
		
		//Find all sessions
		Set<Session> sessions = repo.findAll();
		if (sessions.isEmpty()) {
			
			//If no sessions found, return 204
			return new ResponseEntity<Reply>(new Reply(HttpStatus.NO_CONTENT, "No sessions found to delete."), HttpStatus.NO_CONTENT);
		}
		
		//Remove sessions from respective modules to avoid foreign key constraints.
		for (Session s : sessions) {
			for(Module m: mrepo.findAll()) {
				if(m.getSessions().contains(s)) {
					m.removeSession(s);
				}
			}
			
			//Delete each session
			repo.delete(s);
		}
		
		//Return ok 200
		return new ResponseEntity<Reply>(new Reply(HttpStatus.OK,"All Sessions Deleted."),HttpStatus.OK);
	}
	
	
	//Endpoints 19-20
	@GetMapping("/sessions")
	public ResponseEntity<?> getSessions(@RequestParam(required=false) Optional<Long> convenor, @RequestParam(required=false) Optional<String> module){
		Set<Session> sessions = new HashSet<>();

		
		//Both filters provided
		if(convenor.isPresent() && module.isPresent()) {
			Optional<Convenor> conv = crepo.findById(convenor.get());
			Optional<Module> mod = mrepo.findById(module.get());
			
			//Get's convenor first and checks if module is part of convenors Modules

			if (conv.isPresent() && mod.isPresent()) {
				
				if(conv.get().getModules().contains(mod.get())) {
	
					//Adds all sessions to a list to return
					sessions.addAll(mod.get().getSessions());
						
					//If empty return 204
					if(sessions.isEmpty()) {
						 return new ResponseEntity<Reply>(new Reply(HttpStatus.NO_CONTENT, "No Sessions Found for Convenor: " + conv.get().getName() + " and Module: " + module.get()),HttpStatus.NO_CONTENT);
					}
				}
				else {
					return new ResponseEntity<Reply>(new Reply(HttpStatus.BAD_REQUEST,"Convenor " + convenor.get() + " does not teach Module " + module.get()), HttpStatus.BAD_REQUEST);
				}
			}
			
			
			//If convenor provided doesn't exist return 404
			else if (conv.isEmpty()){
				 return new ResponseEntity<Reply>(new Reply(HttpStatus.NOT_FOUND, "Convenor: " + conv.get().getId() + " does not exist"),HttpStatus.NOT_FOUND);
				}
			
			//If Module doesn't exist return 404.
			else {
				 return new ResponseEntity<Reply>(new Reply(HttpStatus.NOT_FOUND, "Module: " + module.get() + " does not exist"),HttpStatus.NOT_FOUND);

				}
		}
		
			
			
		//If Module isn't provided filter by Module
		else if(convenor.isPresent()) {
			Optional<Convenor> conv = crepo.findById(convenor.get());
			if (conv.isPresent()) {
				
				//Add all sessions from all modules
				for(Module m : conv.get().getModules()) {
					sessions.addAll(m.getSessions());
					}
				
				//If no sessions found return 204
				if (sessions.isEmpty()) {
					return new ResponseEntity<Reply>(new Reply(HttpStatus.NO_CONTENT, "No Sessions found for Convenor " + conv.get().getId()),HttpStatus.NO_CONTENT);
				}
			}
			
			//If convenor doesn't exist return 404
			else {
				 return new ResponseEntity<Reply>(new Reply(HttpStatus.NOT_FOUND, "Convenor: " + convenor.get() + " does not exist"),HttpStatus.NOT_FOUND);
				}
			}
		
		//If just Module provided
		else if(module.isPresent()) {
			Optional<Module> mod = mrepo.findById(module.get());
			
			//Find all sessions for specfic module
			if(mod.isPresent()) {
				sessions.addAll(mod.get().getSessions());
				if(sessions.isEmpty()) {
					
					//Return 204 if no sessions found
					return new ResponseEntity<Reply>(new Reply(HttpStatus.NO_CONTENT, "No Sessions found for Module " + mod.get().getCode()),HttpStatus.NO_CONTENT);
				}
			}
			else {
				
				//Return not found if module doesn't exist
				 return new ResponseEntity<Reply>(new Reply(HttpStatus.NOT_FOUND, "Module: " + module.get() + " does not exist"),HttpStatus.NOT_FOUND);
				}
			}

		//If no filters provided, return all sessions
		else {
			sessions=repo.findAll();
			if(sessions.isEmpty()) {
				return new ResponseEntity<Reply>(new Reply(HttpStatus.NO_CONTENT,"No Sessions Found"),HttpStatus.NO_CONTENT);
			}
		}
		
		//Return Sessions list.
		return new ResponseEntity<Set<Session>>(sessions, HttpStatus.OK);
		
	}
	
}
