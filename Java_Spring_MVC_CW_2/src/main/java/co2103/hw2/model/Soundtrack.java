package co2103.hw2.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
public class Soundtrack {
	
	@Id
	@GeneratedValue
	int id;
	
	String title;
	
	@OneToOne(cascade=CascadeType.MERGE, mappedBy="soundtrack")
	Movie movie;

	@OneToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval=true, fetch=FetchType.EAGER)
	@JoinColumn(name="Soundtrack")
	List<Song> songs = new ArrayList<>();

	
	public int getId() {
		return id;
	}

	public void setId(int soundtrackId) {
		this.id = soundtrackId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public List<Song> getSongs() {
		return songs;
	}

	public void setSongs(Song song) {
		this.songs.add(song);
	}
	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Title: " + title + "\n");
		if(!songs.isEmpty()) {
			sb.append("----Tracklist--- \n");
		}
		return sb.toString();
	}
}