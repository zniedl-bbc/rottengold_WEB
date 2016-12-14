package ch.bbc.rottengold.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.UserTransaction;

import org.primefaces.event.FileUploadEvent;

import ch.bbc.rottengold.ejb.CommentBeanLocal;
import ch.bbc.rottengold.ejb.RatingBeanLocal;
import ch.bbc.rottengold.ejb.UserBeanLocal;
import ch.bbc.rottengold.model.Comment;
import ch.bbc.rottengold.model.User;

@Named
@SessionScoped
public class UserController implements Serializable {

	private static final long serialVersionUID = 4401133314893714110L;

	@Inject
	private User user;

	private User accountviewUser;

	@EJB
	private UserBeanLocal userBean;

	@EJB
	private CommentBeanLocal commentBean;

	@EJB
	private RatingBeanLocal ratingBean;

	@Resource
	private UserTransaction ut;

	private int currentAccount;

	// flag's

	private boolean userLoggedIn = false;
	private boolean usedUsername = false;
	private boolean edditing = false;
	private boolean searchingForAccount = false;
	private Comment commentForAccountUserView;
	private final String PATH = "D:\\Users\\zisler\\Rotten-Gold\\RottenGold_WEB\\WebContent\\img\\profileImg";

	@SuppressWarnings("unused")
	private String profileImgPath;
	private String profileImgPathFromCurrentUser;

	/**
	 * This function is used to register the cached user
	 * 
	 * @return This return string is used that the mainFrame uses the same
	 *         website id again
	 */
	public String register() {

		int result = userBean.checkIfUserAlreadyExists(user);
		if (result == 0) {
			setUsedUsername(false);
			sendConfirmationEmail(user.getEmail());
		} else {
			setUsedUsername(true);
		}

		return "mainFrame?faces-redirect=true&includeViewParams=true";
	}

	public String confirmRegister() {
		userBean.registerUser(user);	
		return "mainFrame?faces-redirect=true&includeViewParams=true";
	}

	/**
	 * This function is used to used to upload profile pictures
	 * 
	 * @param event
	 *            needed to get the inputstream
	 */
	public void handleFileUpload(FileUploadEvent event) {
		try {
			InputStream inputStream = event.getFile().getInputstream();
			File uploads = new File(PATH);

			File file = new File(uploads, user.getUsername() + ".png");

			Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This function sets all the info needed to display the searched user
	 * account
	 * 
	 * @param userId
	 *            searched user
	 * @return This return string is used that the mainFrame uses the same
	 *         website id again
	 */
	public String showAccountInformation(int userId) {
		setAccountviewUser(userBean.getUserById(userId));
		setProfileImgPathFromCurrentUser(getAccountviewUser().getUsername() + ".png");
		setSearchingForAccount(true);
		return "mainFrame?faces-redirect=true&includeViewParams=true";
	}

	/**
	 * This function is used to log the user in
	 * 
	 * @return This return string is used that the mainFrame uses the same
	 *         website id again
	 */
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

	/**
	 * This function is used to log the user out. Invalidates the session
	 * 
	 * @return This return string is used that the mainFrame uses the same
	 *         website id again
	 */
	public String logout() {
		setUserLoggedIn(false);
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "mainFrame?faces-redirect=true&includeViewParams=true";

	}

	/**
	 * This function is used to delete the current logged in user account
	 * 
	 * @return This return string is used that the mainFrame uses the same
	 *         website id again
	 */
	public String deleteAccount() {
		try {
			ut.begin();
			commentBean.deleteCommentsByUserID(user.getId());
			userBean.deleteAccount(user);
			ratingBean.deleteRating(user.getId());
			ut.commit();
		} catch (Exception e) {
			try {
				ut.rollback();
			} catch (Exception e1) {

			}
		}
		logout();
		return "mainFrame?faces-redirect=true&includeViewParams=true";
	}

	/**
	 * This function is used to change the password
	 * 
	 * @return This return string is used that the mainFrame uses the same
	 *         website id again
	 */
	public String changePassword() {
		userBean.changePassword(user);
		return "mainFrame?faces-redirect=true&includeViewParams=true";
	}

	/**
	 * This function sends a confirmation email to the given email
	 * 
	 * @param email
	 *            to email
	 */
	public void sendConfirmationEmail(String email) {
		// Recipient's email ID needs to be mentioned.
		String to = email;

		// Sender's email ID needs to be mentioned
		String from = "no-reply@rottengold.com";

		// Assuming you are sending email from localhost
		String host = "localhost";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject("Confirm your Email!");

			// Now set the actual message
			message.setText("Confirm your E-Mail via this link:");
			message.setText("http://localhost:8080/RottenGold_WEB/faces/confirmUser.xhtml");

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
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
		return getProfileImgPathById(user.getId());
	}

	public void setProfileImgPath(String profileImgPath) {
		this.profileImgPath = profileImgPath;
	}

	public String getProfileImgPathById(int userId) {
		User userFromId = userBean.getUserById(userId);
		profileImgPathFromCurrentUser = userFromId.getUsername() + ".png";
		File f = new File(PATH);
		if (!f.exists()) {
			profileImgPathFromCurrentUser = "default.png";
		}
		return profileImgPathFromCurrentUser;
	}

	public int getCurrentAccount() {
		return currentAccount;
	}

	public void setCurrentAccount(int currentAccount) {
		this.currentAccount = currentAccount;
	}

	public boolean isSearchingForAccount() {
		return searchingForAccount;
	}

	public void setSearchingForAccount(boolean searchingForAccount) {
		this.searchingForAccount = searchingForAccount;
	}

	public User getAccountviewUser() {
		return accountviewUser;
	}

	public void setAccountviewUser(User accountviewUser) {
		this.accountviewUser = accountviewUser;
	}

	public Comment getCommentForAccountUserView() {
		return commentForAccountUserView;
	}

	public void setCommentForAccountUserView(Comment commentForAccountUserView) {
		this.commentForAccountUserView = commentForAccountUserView;
	}

	public String getProfileImgPathFromCurrentUser() {
		return getProfileImgPathById(accountviewUser.getId());
	}

	public void setProfileImgPathFromCurrentUser(String profileImgPathFromCurrentUser) {
		this.profileImgPathFromCurrentUser = profileImgPathFromCurrentUser;
	}

}
