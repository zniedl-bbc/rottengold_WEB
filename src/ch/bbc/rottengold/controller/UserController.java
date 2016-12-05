package ch.bbc.rottengold.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import ch.bbc.rottengold.ejb.CommentBeanLocal;
import ch.bbc.rottengold.ejb.UserBeanLocal;
import ch.bbc.rottengold.model.User;

@Named
@SessionScoped
public class UserController implements Serializable {

	private static final long serialVersionUID = 4401133314893714110L;

	@Inject
	private User user;

	@EJB
	private UserBeanLocal userBean;
	@EJB
	private CommentBeanLocal commentBean;
	
	@Resource
	private UserTransaction ut;
	
	//flag's
	private boolean userLoggedIn = false;
	private boolean usedUsername = false;
	private boolean edditing = false;

	public String register() {
		int result = userBean.registerUser(user);
		if(result == 0){
			setUsedUsername(false);
			return "mainFrame?faces-redirect=true&includeViewParams=true";
		}else{
			setUsedUsername(true);
			return "mainFrame?faces-redirect=true&includeViewParams=true";
		}
	}

	public String login() {
		List<User> tempUser = userBean.checkLogin(user);
		if (tempUser == null || tempUser.isEmpty()) {
			setUserLoggedIn(false);
		} else {
			setUserLoggedIn(true);
			setUser(tempUser.get(0));
			return "mainFrame?faces-redirect=true&includeViewParams=true";
		}
		return "mainFrame?faces-redirect=true&includeViewParams=true";
	}

	public String logout(){
		setUserLoggedIn(false);
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "mainFrame?faces-redirect=true&includeViewParams=true";

	}
	public String deleteAccount(){
		try {
			ut.begin();
			commentBean.deleteCommentsByUserID(user.getId());
			userBean.deleteAccount(user);
			ut.commit();
		} catch (Exception e) {
			try {
				ut.rollback();
			} catch (Exception e1) {
				
			}
		}
		
		return "mainFrame?faces-redirect=true&includeViewParams=true";
	}
	
	public String changePassword(){
		userBean.changePassword(user);
		return "mainFrame?faces-redirect=true&includeViewParams=true";
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	//Flag getters and setters
	public boolean isUserLoggedIn() {
		return userLoggedIn;
	}
	public void setUserLoggedIn(boolean userLoggedIn) {
		this.userLoggedIn = userLoggedIn;
	}
	public boolean isUsedUsername() {
		return usedUsername;
	}
	public void setUsedUsername(boolean usedUsername) {
		this.usedUsername = usedUsername;
	}
	public boolean isEdditing() {
		return edditing;
	}
	public void setEdditing(boolean edditing) {
		this.edditing = edditing;
	}

}
