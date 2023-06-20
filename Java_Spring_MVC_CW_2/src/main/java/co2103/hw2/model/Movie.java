package co2103.hw2.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
public class Movie {

		@Id
		@GeneratedValue
		private int id;

		private String title;
		private String ageRating;
		private int year;
		private int runtime;
		
		@ManyToOne
		@JoinColumn
		private Person director;
		
		@ManyToMany(cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)
		private Set<Person> actors = new HashSet<>();
		
		@OneToOne(cascade= {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE}, orphanRemoval=true)
		@JoinColumn(name="Movie_Soundtrack")
		private Soundtrack soundtrack;
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getAgeRating() {
			return ageRating;
		}
		public void setAgeRating(String ageRating) {
			this.ageRating = ageRating;
		}
		public int getYear() {
			return year;
		}
		public void setYear(int year) {
			this.year = year;
		}
		public int getRuntime() {
			return runtime;
		}
		public void setRuntime(int runtime) {
			this.runtime = runtime;
		}
		public Person getDirector() {
			return director;
		}
		public void setDirector(Person director) {
			this.director = director;
		}
		public Soundtrack getSoundtrack() {
			return soundtrack;
		}
		public void setSoundtrack(Soundtrack soundtrack) {
			this.soundtrack = soundtrack;
		}
		
		public Set<Person> getActors() {
			return actors;
		}
		
		public void setActor(Person actor) {
			this.actors.add(actor);			
		}
		
		public void removeActor(Person person) {
			this.actors.remove(person);
			
		}
		
		/*
		@PreRemove
		public void preRemove() {
			for(Person a : actors) {
				a.removeCastIn(this);
			}
		}*/
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("Tite: " + title + "\n");
			sb.append("Release Year: " + year + "\n");
			sb.append("Age Rating: " + ageRating + "\n");
			sb.append("Runtime: " + runtime + "\n");
			if(director!=null) {
				sb.append("Director: " + director.getFirstName() + " " + director.getLastName() + "\n");
			}
			if(soundtrack!=null) {
				sb.append("Soundtrack: " + soundtrack.getTitle() + "\n");
			}
				
			
			if(!actors.isEmpty()) {
				sb.append("----Cast----\n");
				for(Person a : actors) {
					sb.append(a.getFirstName() + " " + a.getLastName() + "\n");
				}
			}
			return sb.toString();

		}

		
		

	
}
