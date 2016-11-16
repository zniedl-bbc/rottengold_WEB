package ch.bbc.rottengold.controller;

import ch.bbc.rottengold.model.User;

public class SessionUser extends User {

	private static final long serialVersionUID = 1L;
	
	private int id;

	private String email;

	private String password;

	private String username;
	

	public SessionUser() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


}

