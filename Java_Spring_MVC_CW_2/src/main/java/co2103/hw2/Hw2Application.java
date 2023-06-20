package co2103.hw2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import co2103.hw2.model.Movie;
import co2103.hw2.model.Person;
import co2103.hw2.model.Song;
import co2103.hw2.model.Soundtrack;
import co2103.hw2.repo.MovieRepository;
import co2103.hw2.repo.PersonRepository;
import co2103.hw2.repo.SongRepository;
import co2103.hw2.repo.SoundtrackRepository;

@SpringBootApplication
public class Hw2Application implements ApplicationRunner{
	
	
	//Add Autowire Repos
	
	@Autowired
	PersonRepository pRepo;
	
	@Autowired
	MovieRepository mRepo;
	
	@Autowired
	SoundtrackRepository soundRepo;
	
	@Autowired
	SongRepository songRepo;

	public static void main(String[] args) {
		SpringApplication.run(Hw2Application.class, args);
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		//Lists of for randomness
		String[] movieNames= {"Human Of Outer Space", "Man With Four Eyes", "Origin Of The Faceless Ones!"};
		
		String[] age = {"U", "PG", "12A", "12", "15", "18"};
		
		String[] firstName = {"Leslie", "Myah", "Madalyn", "Miranda", "Abbey",
				"Lilly", "Kendal", "Jaylin", "Leanna", "Cassidy",
				"Lindsey", "Anna", "Raymond", "Jonah", "Jude",
				"Valentino", "Reid", "Colin", "Jayce", "Ryker",
				"Rylee", "Felipe", "Malcolm", "Killian"};
		
		String[] lastName = {"Hurst", "Meadows", "Mckay", "Cuevas", "Pittman",
				"Adams", "Marks", "Fitzpatrick", "Higgins", "Potter",
				"Huerta", "Cox", "Ponce", "Mccullough", "Roach",
				"Chen", "Irwin", "Banks", "Rhodes", "Best",
				"Maxwell", "Hodges", "Rowland", "Sosa"};
		
		List<String> songNames = new ArrayList<String>(Arrays.asList("First study", "soul of Nocturn", "only good Focus", "First summer", "soul of piano state", 
				"not quite echoes", "So Classical songbook", "pure Hymn", "still need Lines", 
				"A distilled anthem", "sorrowful ballads", "Nocturne classical romance", 
				"Unexpected brain", "Lighter ballads", "morning choir"," new morning"));
		
		
		//Create Random Object
		Random rand = new Random();
		
		//Create max and Min for year grouping
		int minYear=1950;
		int maxYear=2020;
		
		//Create Max and Min for Movie Runtime
		int minRun=70;
		int maxRun=135;
		
		int maxName=firstName.length;
		
		//Create lists for objects to save to database
		List<Movie> movies = new ArrayList<>();
		List<Person> directors = new ArrayList<>();
		List<Person> actors = new ArrayList<>();
		List<Person> composers = new ArrayList<>();
		List<Song> songs = new ArrayList<>();

		//Create movie datbase objects and soundtracks
		for(int i=0; i<movieNames.length; i++) {
			Movie movie=new Movie();
			Soundtrack s = new Soundtrack();
			s.setTitle(movieNames[i] + ": The Soundtrack");
			movie.setSoundtrack(s);
			movie.setTitle(movieNames[i]);
			movie.setYear(rand.nextInt(maxYear-minYear)+minYear);
			movie.setRuntime(rand.nextInt(maxRun-minRun)+minRun);
			movie.setAgeRating(age[rand.nextInt(6)]);
			movies.add(movie);
		
		}
		
		//Save movies to the database
		for(Movie m:movies) {
			m=mRepo.save(m);
		}
		
		//Generate 3 random actors
		for(int i=0; i<3; i++) {
			Person p = new Person();
			p.setFirstName(firstName[rand.nextInt(maxName)]);
			p.setLastName(lastName[rand.nextInt(maxName)]);
			p.setJobTitle("Actor");
			actors.add(p);
			}
		
		//Generate 2 random directors
		for(int i=0; i<2; i++) {
			Person p = new Person();
			p.setFirstName(firstName[rand.nextInt(maxName)]);
			p.setLastName(lastName[rand.nextInt(maxName)]);
			p.setJobTitle("Director");
			directors.add(p);
			}
		
		
		//Generate 2 random composers
		for(int i=0; i<2; i++) {
			Person p = new Person();
			p.setFirstName(firstName[rand.nextInt(maxName)]);
			p.setLastName(lastName[rand.nextInt(maxName)]);
			p.setJobTitle("Composer");
			composers.add(p);
			}
		
		//Save all people to the database
		for(Person d : directors) {
			d=pRepo.save(d);
		}
		for(Person a: actors) {
			a=pRepo.save(a);
		}
		for(Person c : composers) {
			c=pRepo.save(c);
		}
	
		
		//Randomly assign directors and actors to each movie
		for(Movie m: movies) {
			m.setDirector(directors.get(rand.nextInt(directors.size())));
			
			//Set a random number of actors to each movie
			for(int i=0; i<rand.nextInt(4)+2;i++) {
				int randActor=rand.nextInt(actors.size());
				m.setActor(actors.get(randActor));
			}
			
			//Update movie in datbaase
			m=mRepo.save(m);
		}
		
		//Create songs and save them each sound track
		//First Pull all the soundtrack objects from the database
		for(Soundtrack s : soundRepo.findAll()) {
			int songMax = rand.nextInt(2)+1;
			
			//For Each sound track set 1-2 songs.
			for(int i=0; i<songMax; i++){
				
				Song song = new Song();
				song.setTrackNumber(i+1);
				
				//Generate random runtime values
				int minute = rand.nextInt(10)+1;
				int second = rand.nextInt(60);
				int index = rand.nextInt(songNames.size());
				
				//Randomly get song name
				song.setTitle(songNames.get(index));
				
				//Remove song name from list
				songNames.remove(index);
				
				//Add formatting and set runtime
				song.setRuntime(String.valueOf(minute) + ":" + String.format("%02d", second));
				
				//Set the composer of the song
				song.setComposer(composers.get(rand.nextInt(composers.size())));
				
				//Set the song to the sound track
				s.setSongs(song);
			}
			
			//Update the soundtrack
			s=soundRepo.save(s);
		}
		
	}
}
