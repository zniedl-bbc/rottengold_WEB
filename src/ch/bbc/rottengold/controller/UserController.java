package ch.bbc.rottengold.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

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
	// flag's
	private boolean userLoggedIn = false;
	private boolean usedUsername = false;
	private boolean edditing = false;

	private String profileImgPath;

	public String register() {
		int result = userBean.registerUser(user);
		if (result == 0) {
			setUsedUsername(false);
			return "mainFrame?faces-redirect=true&includeViewParams=true";
		} else {
			setUsedUsername(true);
			return "mainFrame?faces-redirect=true&includeViewParams=true";
		}
	}

	public void handleFileUpload(FileUploadEvent event) {
		try {
			InputStream inputStream = event.getFile().getInputstream();
			File uploads = new File("D:\\Users\\zniedl\\Rotten-Gold\\RottenGold_WEB\\WebContent\\img\\profileImg");
			File file = new File(uploads, user.getUsername() + ".png");

			Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public String logout() {
		setUserLoggedIn(false);
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "mainFrame?faces-redirect=true&includeViewParams=true";

	}

	public String deleteAccount() {
		userBean.deleteAccount(user);
		return "mainFrame?faces-redirect=true&includeViewParams=true";
	}

	public String changePassword() {
		userBean.changePassword(user);
		return "mainFrame?faces-redirect=true&includeViewParams=true";
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	// Flag getters and setters
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

	public String getProfileImgPath() {
		return user.getUsername() + ".png";
	}

	public void setProfileImgPath(String profileImgPath) {
		this.profileImgPath = profileImgPath;
	}

}
