package edu.leicester.co2103.controller;

import java.util.Set;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import edu.leicester.co2103.repo.ConvenorRepository;
import edu.leicester.co2103.repo.ModuleRepository;
import edu.leicester.co2103.repo.SessionRepository;
import edu.leicester.co2103.domain.Convenor;
import edu.leicester.co2103.domain.Module;
import edu.leicester.co2103.domain.Reply;
import edu.leicester.co2103.domain.Session;



@RestController
public class ModuleRestController {
	
	@Autowired
	ModuleRepository repo;
	
	@Autowired
	ConvenorRepository crepo;
	
	@Autowired
	SessionRepository srepo;
	
	//Endpoint 7
	@GetMapping("/modules")
	public ResponseEntity<?> SetAllModules() {
		
		//Get all Modules
		Set<Module> modules = repo.findAll();
		if (modules.isEmpty()) {
			 
			 //Return 204 message
			 Reply error=new Reply(HttpStatus.NO_CONTENT);
			 error.setMessage("No Modules Found");
			 return new ResponseEntity<Reply>(error,HttpStatus.NO_CONTENT);
		 }
		 
		 //Return module list
		 return new ResponseEntity<Set<Module>>(modules, HttpStatus.OK);
	}

	
	//Endpoint 8
	@PostMapping("/modules")
	public ResponseEntity<?> createModule(@RequestBody Module module) {
			
		//Check if Module already exists
		if (repo.existsById(module.getCode())) {
			 Reply error = new Reply(HttpStatus.CONFLICT, "Module with Module code: " + module.getCode() + " already exists.");
			 return new ResponseEntity<Reply>(error, HttpStatus.CONFLICT);
		 }
		 
		//Save new Module
		module = repo.save(module);
		 
		//Return Created successful 201
		Reply created = new Reply(HttpStatus.CREATED, "Location: http://localhost:8080/modules/"+module.getCode());
		return new ResponseEntity<Reply>(created, HttpStatus.CREATED);
	}
	
	//Enpoint 9
	@GetMapping("/modules/{code}")
	public ResponseEntity<?> getModule(@PathVariable("code") String code){
		Optional<Module> module= repo.findById(code);
		 
		//Check if module exists
		if (module.isEmpty()) {
			return new ResponseEntity<Reply>(new Reply(HttpStatus.NOT_FOUND, "Module " + code + " does not exist."), HttpStatus.NOT_FOUND);
		}
		
		//Return module
		return new ResponseEntity<Module>(module.get(), HttpStatus.OK);
	}
	
	
	//Endpoint 10
	@PatchMapping("/modules/{code}")
	public ResponseEntity<?> updateModule(@PathVariable("code") String code, @RequestBody Module 
	module) {
		Optional<Module> getModule = repo.findById(code);
		
		//Check if module exists
		if (getModule.isEmpty()) {
			return new ResponseEntity<Reply>(new Reply(HttpStatus.NOT_FOUND, "Module " + code + " does not exist."), HttpStatus.NOT_FOUND);
		}
		
		//set current module
		Module currentModule=getModule.get();
	 	
		//Ensure provided level is valid
		if (module.getLevel()!=0) {
			currentModule.setLevel(module.getLevel());
		}

		//Check if a title has been provided
		if (module.getTitle()!=null) {
			currentModule.setTitle(module.getTitle());
		}
	 
		//check if optional has been provided
		if (module.isOptional()!=null) {
			currentModule.setOptional(module.isOptional());
		}
		
		//Check if sessions have been provided
		if (module.getSessions()!=null && !module.getSessions().isEmpty()) {
			
			//Loop through provided sessions
			for (Session s : module.getSessions()) {
				
				//Check if session already exists
				if(srepo.findById(s.getId()).isPresent()) {
			 
					//Create new RestTemplate to patch existing session
					RestTemplate rest = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
					HttpEntity<Session> hs = new HttpEntity<Session>(s);
	 				String uri = "http://localhost:8080/modules/" + currentModule.getCode() + "/sessions/" + s.getId();
					
	 				//Patch session
	 				rest.exchange(uri, HttpMethod.PATCH, hs, Session.class, s);
					
	 				

				}	
				else {
					currentModule.addSession(s);
				}
			}
		}
		
		//Save updated module
		repo.save(currentModule);
		
		//Refresh the currentModule before sending
		repo.refresh(currentModule);
		return new ResponseEntity<Module>(currentModule, HttpStatus.OK);
	}
	
	
	//Endpoint 11
	@DeleteMapping("/modules/{code}")
	public ResponseEntity<?> deleteModule(@PathVariable("code") String code) {
		
		//Check if Module exists
		Optional<Module> m = repo.findById(code);
		if(m.isPresent()){
			Set<Convenor> convenors = crepo.findByModulesCode(code);
			
			//Remove Module from Convenor's lists
			for (Convenor c : convenors) {
				c.removeModule(m.get());
				crepo.save(c);
			}
			
			//Delete Module and return success
			repo.delete(m.get());
			return new ResponseEntity<Reply>(new Reply(HttpStatus.OK,"Module " + code + " deleted."), HttpStatus.OK);
		}
		
		//If not found return 404
		else {
			return new ResponseEntity<Reply>(new Reply(HttpStatus.NOT_FOUND, "Module " + code + " does not exist."), HttpStatus.NOT_FOUND);
		}
	}
	
	
	//Endpoint 12
	@GetMapping("/modules/{code}/sessions")
	public ResponseEntity<?> SetModuleSessions(@PathVariable("code") String code){
		 Optional<Module> module= repo.findById(code);
		 
		 //Check if module exists
		 if (module.isEmpty()) {
			 return new ResponseEntity<Reply>(new Reply(HttpStatus.NOT_FOUND, "Module " + code + " does not exist."), HttpStatus.NOT_FOUND);
		 }
		 
		 //Check if session list is empty
		 else if (module.get().getSessions().isEmpty()) {
			 return new ResponseEntity<Reply>(new Reply(HttpStatus.NO_CONTENT,"No sessions found for Module "+code),HttpStatus.NO_CONTENT);
		 }
		 
		 //Return session list
		 else {
			 return new ResponseEntity<Set<Session>>(module.get().getSessions(), HttpStatus.OK);
		}
	}

	//Endpoint 13
	@PostMapping("/modules/{code}/sessions")
	public ResponseEntity<?> createSession(@PathVariable("code") String code, @RequestBody Session session) {
		
		//Check if session already exists
		if (srepo.existsById(session.getId())) {
				return new ResponseEntity<Reply>(new Reply(HttpStatus.CONFLICT,"Session " + session.getId() + " already exists."),HttpStatus.CONFLICT);
		}
		
		//Check that module exists
		if(repo.existsById(code)) {
			
			//Save session and add session to Module
			session=srepo.save(session);
			Module m = repo.findById(code).get();
			m.addSession(session);
			repo.save(m);
		}
		
		//If module does not exists return 404
		else {
			return new ResponseEntity<Reply>(new Reply(HttpStatus.NOT_FOUND,"Module " + code + " does not exist."),HttpStatus.NOT_FOUND);
		}
	  
		//send created message with location
	 	Reply created = new Reply(HttpStatus.CREATED,"http://localhost:8080/modules/"+code+"/sessions/" + session.getId());
	 	return new ResponseEntity<Reply>(created, HttpStatus.CREATED);
	}
	
	
	//Endpoint 14
	@GetMapping("/modules/{code}/sessions/{id}")
	public ResponseEntity<?> getModuleSession(@PathVariable("code") String code, @PathVariable("id") Long id){
		 Optional<Session> session= srepo.findById(id);
		 
		 //Check that Module exists
		 if (repo.findById(code).isEmpty()) {
			 return new ResponseEntity<Reply>(new Reply(HttpStatus.NOT_FOUND, "Module " + code +" does not exist."), HttpStatus.NOT_FOUND);
		 }
		 
		 //Check that session exists
		 else if (session.isEmpty()) {
			 return new ResponseEntity<Reply>(new Reply(HttpStatus.NOT_FOUND, "Session " +id + " does not exist."), HttpStatus.NOT_FOUND);
		 }
		 
		 //If both exist, ensure module owns the session
		 else if(repo.findById(code).get().getSessions().contains(session.get())) {
			 return new ResponseEntity<Session>(session.get(), HttpStatus.OK);
		 }
		 
		 //If session exists but is not owned by the module, return 404
		 else {
			 return new ResponseEntity<Reply>(new Reply(HttpStatus.NOT_FOUND, "Session " +id + " does belong to Module " + code), HttpStatus.NOT_FOUND);
		 }
		}
	

	//Endpoint 15
	@PutMapping("/modules/{code}/sessions/{id}")
	public ResponseEntity<?> updateSession(@PathVariable("code") String code, @PathVariable("id") Long id, @RequestBody Session session){
		Optional<Module> getModule = repo.findById(code);
		Optional<Session> getSession = srepo.findById(id);

		if (getModule.isEmpty()) {
			 return new ResponseEntity<Reply>(new Reply(HttpStatus.NOT_FOUND, "Module " + code +" does not exist."), HttpStatus.NOT_FOUND);
		}
		else if (getSession.isEmpty()) {
			 return new ResponseEntity<Reply>(new Reply(HttpStatus.NOT_FOUND, "Session " +id + " does not exist."), HttpStatus.NOT_FOUND);
		}
		else {
			Session currentSession = getSession.get();
			if (getModule.get().getSessions().contains(getSession.get())){
				if(session.getDatetime()!=null && session.getDuration()>=0 && session.getTopic()!=null && !session.getTopic().isEmpty()) {
					currentSession.setDatetime(session.getDatetime());
					currentSession.setDuration(session.getDuration());
					currentSession.setTopic(session.getTopic());
					srepo.save(currentSession);
					return new ResponseEntity<Session>(currentSession, HttpStatus.OK);
				}
				else {
					 String msg = "PUT Request must contain all session elements, topic, datetime, and duration";
					return new ResponseEntity<Reply>(new Reply(HttpStatus.BAD_REQUEST,msg),HttpStatus.BAD_REQUEST);
				}
			}
		}
		 return new ResponseEntity<Reply>(new Reply(HttpStatus.NOT_FOUND, "Session " + id +" or Module " + code + " does not exist."), HttpStatus.NOT_FOUND);
	}
	
	//Endpoint 16
	@PatchMapping("/modules/{code}/sessions/{id}")
	public ResponseEntity<?> modifySessions(@PathVariable("code") String code, @PathVariable("id") Long id, @RequestBody Session session) {
		Optional<Module> getModule = repo.findById(code);
		Optional<Session> getSession = srepo.findById(id);
		Session currentSession = new Session();
		
		//Check if Module exists
		if (getModule.isEmpty()) {
			 return new ResponseEntity<Reply>(new Reply(HttpStatus.NOT_FOUND, "Module " + code +" does not exist."), HttpStatus.NOT_FOUND);
		}
		
		//Check if session exists
		else if (getSession.isEmpty()) {
			 return new ResponseEntity<Reply>(new Reply(HttpStatus.NOT_FOUND, "Session " +id + " does not exist."), HttpStatus.NOT_FOUND);
		}
		
		//Check that the session belongs to the correct module
		else {
			if (getModule.get().getSessions().contains(getSession.get())){
				currentSession = getSession.get();
			}
			else {
				return new ResponseEntity<Reply>(new Reply(HttpStatus.NOT_FOUND, "Session " + id + " does not belong to Module " +code), HttpStatus.NOT_FOUND);
			}
		
		}
		
		//Check if topic has been provided
		if (session.getTopic()!=null && !session.getTopic().isEmpty()) {
			 currentSession.setTopic(session.getTopic());
		 }
		
		//check if time has been provided
		 if (session.getDatetime()!=null) {
			 currentSession.setDatetime(session.getDatetime());
		 }
		 
		 //check if duration has been provided
		 if (session.getDuration()>0) {
			 currentSession.setDuration(session.getDuration());
		 }
		 
		 //Save session
		 currentSession = srepo.save(currentSession);
		 return new ResponseEntity<Session>(currentSession, HttpStatus.OK);
	}
	
	
	//Endpoint 17
	@DeleteMapping("/modules/{code}/sessions/{id}")
	public ResponseEntity<?> deleteSession(@PathVariable("code") String code, @PathVariable Long id) {
		Optional<Module> m = repo.findById(code);
		Optional<Session> s = srepo.findById(id);
		
		//Confirm that both module and session are present
		if(m.isPresent() && s.isPresent()){
			
			//Check if session is in modules list
			if (m.get().getSessions().contains(s.get())) {
				
				//Remove session from module list to avoid foreign key issue.
				m.get().removeSession(s.get());
				
				//delete from database
				srepo.delete(s.get());
				
				//return ok 200
				return new ResponseEntity<Reply>(new Reply(HttpStatus.OK,"Session " + id + " for Module " + code + " deleted."),HttpStatus.OK);
			}
			else {
				
				//Return 400 if module does not contain specified session
				return new ResponseEntity<Reply>(new Reply(HttpStatus.BAD_REQUEST, "Session " + id + " does not belong to Module " + code),HttpStatus.BAD_REQUEST);

			}
		}
		
		//Returns 404 if Module is not found
		else if (m.isEmpty()) {
			return new ResponseEntity<Reply>(new Reply(HttpStatus.NOT_FOUND, "Module " + code + " not found."),HttpStatus.NOT_FOUND);

		}
		
		//Returns 404 if Session is not found
		else {
			return new ResponseEntity<Reply>(new Reply(HttpStatus.NOT_FOUND, "Session " + id + " not found."),HttpStatus.NOT_FOUND);

		}

	}
}
	

