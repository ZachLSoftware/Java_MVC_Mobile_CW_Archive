package edu.leicester.co2103.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

@Entity
public class Convenor {

	@Id
	@GeneratedValue
	private long id;

	private String name;
	private Position position;

	//Replaced List with Set to avoid issues with join duplicates in get requests.
	@ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JoinColumn
	private Set<Module> modules;

	public Convenor(String name, Position position) {
		this(name, position, new HashSet<>());
	}
	public Convenor() {
		this.modules=new HashSet<>();
	}

	public Convenor(String name, Position position, Set<Module> modules) {
		super();
		this.name = name;
		this.position = position;
		this.modules = modules;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Set<Module> getModules() {
		return modules;
	}

	public void setModules(Set<Module> modules) {
		this.modules = modules;
	}
	
	public void removeModule(Module mod) {
		this.modules.remove(mod);
	}

}
