package ch.bbc.rottengold.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import ch.bbc.rottengold.ejb.UserBeanLocal;
import ch.bbc.rottengold.model.User;

@Named
@SessionScoped
public class UserController implements Serializable {

	private static final long serialVersionUID = 4401133314893714110L;
	
	@Inject
	User user;
	
	@EJB
	UserBeanLocal userBean;
	
	public String switchToSignUp() {
		return "/signUp";
	}

	public String switchToLogin() {
		return "/login";
	}
	
	public String register(){
		return userBean.registerUser(user);
	}
	public String login(){
		List<User> tempUser = userbean.checkLogin(user);
		creatSession(tempUser.get(0));
		return "/home";
	}
	
	private void creatSession(User user) {
		user.setCreatedAt(new Date());
		
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
