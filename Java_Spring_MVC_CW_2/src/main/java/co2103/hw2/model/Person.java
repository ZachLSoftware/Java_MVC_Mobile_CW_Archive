package co2103.hw2.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PreRemove;

@Entity
public class Person {
	
	@Id
	@GeneratedValue
	private int id;
	

	private String firstName;
	private String lastName;
	private String jobTitle;
	
	
	@ManyToMany(mappedBy="actors", fetch=FetchType.LAZY)
	private Set<Movie> castIn = new HashSet<>();
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	

	public Set<Movie> getCastIn() {
		return castIn;
	}
	
	public void setCastIn(Movie movie) {
		castIn.add(movie);
	}
	

	@PreRemove
	public void preRemove() {
		if(jobTitle.equals("Actor")) {
			for(Movie m : castIn) {
				m.removeActor(this);				
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: " + firstName + " " + lastName + " - ");
		sb.append(jobTitle);
		return sb.toString();
	}
	
}
