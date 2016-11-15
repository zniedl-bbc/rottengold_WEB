package ch.bbc.rottengold.controller;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ch.bbc.rottengold.ejb.UserBeanLocal;
import ch.bbc.rottengold.model.User;

@Named
@SessionScoped
public class UserController implements Serializable {

	private static final long serialVersionUID = 4401133314893714110L;
	
	@Inject
	User user;
	
	@EJB
	UserBeanLocal userbean;
	
	public String switchToSignUp() {
		return "/signUp";
	}

	public String switchToLogin() {
		return "/login";
	}
	
	public String register(){
		return userbean.registerUser(user);
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
