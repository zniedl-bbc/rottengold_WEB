package ch.bbc.rottengold.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
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

	private boolean userLoggedIn = false;

	public String switchToSignUp() {
		return "/signUp";
	}

	public String switchToLogin() {
		return "/login";
	}

	public String register() {
		return userBean.registerUser(user);
	}

	public String login() {
		List<User> tempUser = userBean.checkLogin(user);
		if (tempUser == null || tempUser.isEmpty()) {
			setUserLoggedIn(false);
		} else {
			setUserLoggedIn(true);
		}
		return "/home";
	}
	public String logout(){
		setUserLoggedIn(false);
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "";
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isUserLoggedIn() {
		return userLoggedIn;
	}

	public void setUserLoggedIn(boolean userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}
}
