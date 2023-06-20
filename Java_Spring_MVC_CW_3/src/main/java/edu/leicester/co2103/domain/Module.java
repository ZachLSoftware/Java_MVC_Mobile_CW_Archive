package edu.leicester.co2103.domain;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Module {

	@Id
	private String code;
	private String title;
	private int level;
	private Boolean optional;

	//Replaced List with Set to avoid issues with join duplicates in get requests.
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn (name = "module")
	private Set<Session> sessions;

	public Module() {
		sessions = new HashSet<Session>();
	}

	public Module(String code, String title, int level, boolean optional) {
		this(code, title, level, optional, new HashSet<>());
	}

	public Module(String code, String title, int level, boolean optional, Set<Session> sessions) {
		this.code = code;
		this.title = title;
		this.level = level;
		this.optional = optional;
		this.sessions = sessions;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Boolean isOptional() {
		return optional;
	}

	public void setOptional(Boolean optional) {
		this.optional = optional;
	}

	public Set<Session> getSessions() {
		return sessions;
	}

	public void addSession(Session session) {
		this.sessions.add(session);
	}
	public void removeSession(Session session) {
		this.sessions.remove(session);
	}
}
