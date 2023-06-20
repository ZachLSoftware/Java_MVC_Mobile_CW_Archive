package edu.leicester.co2103.domain;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

//Class to stamp replies and give a more readable message
public class Reply {
	public HttpStatus status;
	public LocalDateTime timestamp;
	public String replyMessage;
	
	public Reply() {
		this.timestamp = LocalDateTime.now();
	}
	
	public Reply(HttpStatus status) {
		this.timestamp = LocalDateTime.now();
		this.status=status;
	}
	
	public Reply(HttpStatus status, String msg) {
		this.timestamp = LocalDateTime.now();
		this.status=status;
		this.replyMessage=msg;
	}
	
	public void setStatus(HttpStatus status) {
		this.status=status;
	}
	
	public void setMessage(String msg) {
		this.replyMessage=msg;
	}

}
