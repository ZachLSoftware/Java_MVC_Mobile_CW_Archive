package edu.leicester.co2103.controller;


import java.util.HashSet;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import edu.leicester.co2103.domain.Convenor;
import edu.leicester.co2103.domain.Module;
import edu.leicester.co2103.domain.Reply;
import edu.leicester.co2103.repo.ConvenorRepository;
import edu.leicester.co2103.repo.ModuleRepository;


@RestController
public class ConvenorRestController {
	@Autowired
	ConvenorRepository repo;
	
	@Autowired
	ModuleRepository mrepo;
	
	
	//Endpoint 1
	@GetMapping("/convenors")
	public ResponseEntity<?> SetAllConvenors() {
		 Set<Convenor> convenors = repo.findAll();
		 
		 //Returns 204 if no convenors available
		 if (convenors.isEmpty()) {
			 return new ResponseEntity<Reply>(new Reply(HttpStatus.NO_CONTENT, "No Convenors Found"), HttpStatus.NO_CONTENT);
		 }
		 return new ResponseEntity<Set<Convenor>>(convenors, HttpStatus.OK);
		}

	
	//Endpoint 2
	@PostMapping("/convenors")
	public ResponseEntity<?> createConvenor(@RequestBody Convenor convenor) {
		
		 //Check if Convenor already exists	
		 if (repo.existsById(convenor.getId())) {
			 
			 //Returns a conflict message if exists
			 return new ResponseEntity<Reply>(new Reply(HttpStatus.CONFLICT,"Convenor with ID " + convenor.getId() + " already exists."),HttpStatus.CONFLICT);
		 }
		 
		 //Gets modules and checks if they exists already.  Patches them if included in case of modifications
		 for(Module m : convenor.getModules()) {
			 if(mrepo.findById(m.getCode()).isPresent()) {
				 RestTemplate rest = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
	
				 HttpEntity<Module> hm = new HttpEntity<Module>(m);
				 
				 String uri = "http://localhost:8080/modules/" + m.getCode();
				 rest.exchange(uri, HttpMethod.PATCH, hm, Module.class, m);
				 
			 }
			 
			 //If modules don't already exist, create them.
			 else {
				 RestTemplate rest = new RestTemplate();
				 String uri="http://localhost:8080/modules/";
				 rest.postForObject(uri, m, ResponseEntity.class);
				 
			 }
		 }
		 
		 //Save new Convenor
		 convenor = repo.save(convenor);
		 
		 //Return location of new convenor
		 return new ResponseEntity<Reply>(new Reply(HttpStatus.CREATED, "Location: http://localhost:8080/convenors/" +convenor.getId()), HttpStatus.CREATED);
	}

	//Endpoint 3
	@GetMapping("/convenors/{id}")
	public ResponseEntity<?> getConvenor(@PathVariable("id") Long id){
		
		//Confirm convenor exists.
		 Optional<Convenor> convenor = repo.findById(id);
		 if (convenor.isEmpty()) {
			 return new ResponseEntity<Reply>(new Reply(HttpStatus.NOT_FOUND,"Convenor with ID "+ id + " not found."), HttpStatus.NOT_FOUND);
		 }
		 return new ResponseEntity<Convenor>(convenor.get(), HttpStatus.OK);
		}
	
		

	
	//Endpoint 4
		@PutMapping("/convenors/{id}")
		public ResponseEntity<?> updateConvenor(@PathVariable("id") Long id, @RequestBody Convenor 
		convenor) {
			
			//Check if convenor exists
			 Optional<Convenor> getConvenor = repo.findById(id);
			 if (getConvenor.isEmpty()) {
				 
				 //Return 404 if not found
				 return new ResponseEntity<Reply>(new Reply(HttpStatus.NOT_FOUND, "Convenor with ID "+ id + " not found."), HttpStatus.NOT_FOUND);
			 }
			 
			 //Ensure put request has all necessary info provided
			 if (convenor.getName()==null || convenor.getName().isEmpty() || convenor.getPosition()==null) {
				 String msg = "PUT Request must contain Name and Position.";
				
				 return new ResponseEntity<Reply>(new Reply(HttpStatus.BAD_REQUEST,msg), HttpStatus.BAD_REQUEST);
			 }
			 
			 //PUT convenor
			 Convenor currentConvenor=getConvenor.get();
			 currentConvenor.setName(convenor.getName());
			 currentConvenor.setPosition(convenor.getPosition());
			 if (convenor.getModules()==null || convenor.getModules().isEmpty()) {
				 convenor.setModules(new HashSet<>());
				 
			 }
			 
			 currentConvenor.setModules(convenor.getModules());
			 
			 //Save convenor
			 currentConvenor = repo.save(currentConvenor);
			 
			 //Return convenor and OK
			 return new ResponseEntity<Convenor>(currentConvenor, HttpStatus.OK);
		}
	
	//Endpoint 5
	@DeleteMapping ("/convenors/{id}")
	public ResponseEntity<?> deleteConvenor(@PathVariable("id") Long id) {
		
		//Confirm Convenor exists
		Optional<Convenor> c = repo.findById(id);
		if(c.isPresent()) {
			Convenor cv = c.get();
			
			//Check if Module is attached to more than 1 convenor.  If it is, remove it from module list to avoid deletion or foreign key errors.
			Set<Module> mods =new HashSet<>(cv.getModules());
			for(Module mod : mods) {
				if(repo.findByModulesCode(mod.getCode()).size()>1) {
					cv.removeModule(mod);
				}
			}
			repo.delete(cv);
			
			//Return successful
			return new ResponseEntity<Reply>(new Reply(HttpStatus.OK, "Convenor with ID "+ id + " deleted successfully."),HttpStatus.OK);
		}
		else {
			
			//If convenor doesn't exists return 404
			return new ResponseEntity<>(new Reply(HttpStatus.NOT_FOUND, "Convenor with ID "+ id + " not found."), HttpStatus.NOT_FOUND);
		}
	}
	
	//Endpoint 6
	@GetMapping("/convenors/{id}/modules")
	public ResponseEntity<?> SetConvenorModules(@PathVariable("id") Long id){
		
		//Confirm convenor exists
		 Optional<Convenor> convenor = repo.findById(id);
		 if (convenor.isEmpty()) {
			 return new ResponseEntity<>(new Reply(HttpStatus.NOT_FOUND,"Convenor with ID "+ id + " not found."), HttpStatus.NOT_FOUND);
		 }
		 
		 //If Convenor has no modules return 204.
		 if (convenor.get().getModules().isEmpty()) {
			 return new ResponseEntity<Reply>(new Reply(HttpStatus.NO_CONTENT,"No Modules found for Convenor: " + id), HttpStatus.NO_CONTENT);
		 }
		 
		 //Return module list.
		 return new ResponseEntity<Set<Module>>(convenor.get().getModules(), HttpStatus.OK);
		
	}
		
}
