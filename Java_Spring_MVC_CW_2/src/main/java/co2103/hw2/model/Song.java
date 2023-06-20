package co2103.hw2.model;

import javax.persistence.*;

@Entity
public class Song {

	@Id
	@GeneratedValue
	private int id;
	
	private int trackNumber;
	
	private String title;
	
	private String runtime;
		
	@ManyToOne
	@JoinColumn
	private Person composer;

	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(int trackNumber) {
		this.trackNumber = trackNumber;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}


	public Person getComposer() {
		return composer;
	}

	public void setComposer(Person composer) {
		this.composer = composer;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Track " + trackNumber + ": ");
		sb.append(title + " Runtime: ");
		sb.append(runtime + " ");
		if(composer!=null) {
			sb.append("Composer: " + composer.getFirstName() + " " + composer.getLastName());
		}
		return sb.toString();
	}	
}
