package ch.bbc.rottengold.controller;

import java.io.Serializable;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import ch.bbc.rottengold.ejb.CommentBeanLocal;
import ch.bbc.rottengold.ejb.UserBeanLocal;
import ch.bbc.rottengold.ejb.WebsiteInfoBeanLocal;
import ch.bbc.rottengold.model.Comment;
import ch.bbc.rottengold.model.Website;

@Named
@ViewScoped
public class CommentController implements Serializable {

	private static final long serialVersionUID = -2587101126657460690L;

	@EJB
	private CommentBeanLocal commentBean;

	@EJB
	private WebsiteInfoBeanLocal websiteInfoBean;

	@EJB
	private UserBeanLocal userBean;

	@Inject
	private UserController userController;

	private int websiteId;

	private Comment[] comments;

	private Comment toBeDeletedComment;

	private Comment toBeEditedComment;

	private int userIdOfSelectedComment;

	@Inject
	private Comment newComment;

	private Website website;

	// flags
	private boolean idFound;
	private boolean editing;

	/**
	 * This function is called whenever the CommentController is called in a new
	 * view and initializes the id for the initCommentsAndInfo function
	 */
	@PostConstruct
	public void init() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		int idURLParam = 0;
		idFound = false;
		if (request.getParameter("id") != null) {
			idURLParam = Integer.parseInt(request.getParameter("id"));
			if (idURLParam == 0) {
				idURLParam = 1;
			}
			userController.setSearchingForAccount(false);
			initCommentsAndInfo(idURLParam);

		} else {
			idURLParam = 1;
			initCommentsAndInfo(idURLParam);
		}
		if (idURLParam <= websiteInfoBean.findBiggestWebsiteId()) {
			idFound = true;
		} else {
			idFound = false;
		}

		editing = false;
	}

	/**
	 * This function is used to initialize all the comments from the database
	 * whenever this controller is called as a new view
	 * 
	 * @param idURLParam
	 *            id to get comments from database
	 */
	private void initCommentsAndInfo(int idURLParam) {
		setWebsiteId(idURLParam);
		comments = null;
		setComments(commentBean.getCommentsViaWebsite(idURLParam, websiteInfoBean.findBiggestWebsiteId()));
		if (comments != null) {
			for (Comment c : comments) {
				if (c.getId_website() != websiteId)
					comments = null;
			}	
		}
		if (comments != null){
			comments = reverseComments(comments);
		}
	}

	/**
	 * This function reverses all the comments that the newest comment is the
	 * first in the array
	 * 
	 * @param comments
	 *            Array to reverse
	 * @return Reversed array
	 */
	private Comment[] reverseComments(Comment[] comments) {
		Comment[] tempCommentArray = comments;
		int counter = 0;
		int counterForExchange = comments.length;
		comments = new Comment[tempCommentArray.length];
		while (counter != comments.length) {
			counterForExchange--;
			comments[counter] = tempCommentArray[counterForExchange];
			counter++;
		}
		return comments;
	}

	/**
	 * This function checks whether the logged in user is the writer of a
	 * comment or not
	 * 
	 * @param comment
	 *            to be checked comment
	 * 
	 * @return returns result of check
	 */
	public boolean isUserCommentWriter(Comment comment) {
		if (userController.getUser().getId() == comment.getId_user()) {
			return true;
		}

		return false;
	}

	/**
	 * This function is used to get user information from the UserController
	 * with the user id
	 * 
	 * @return returns empty string because the view shouldn't change
	 */
	public String showAccountInformation() {
		getUserController().showAccountInformation(getUserIdOfSelectedComment());
		return "";
	}

	/**
	 * This function is used to add a new comment
	 * 
	 * @return This return string is used that the mainFrame uses the same
	 *         website id again
	 */
	public String addNewComment() {
		newComment.setId_website(getWebsiteId());
		newComment.setId_user(getUserController().getUser().getId());
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		newComment.setCreationdate(dateFormat.format(date));
		commentBean.addComment(newComment);
		newComment.clear();
		userBean.increaseCommentCounter(getUserController().getUser());
		return "mainFrame?faces-redirect=true&includeViewParams=true";

	}

	public String changeToEditMode() {
		editing = true;
		return null;
	}

	public String editComment() {
		commentBean.editComment(toBeEditedComment);
		editing = false;
		return null;
	}

	/**
	 * This function is used to delete a comment
	 * 
	 * @return This return string is used that the mainFrame uses the same
	 *         website id again
	 */
	public String deleteComment() {
		commentBean.deleteComment(toBeDeletedComment.getId());
		userBean.decreaseCommentCounter(getUserController().getUser());
		return "mainFrame?faces-redirect=true&includeViewParams=true";
	}

	public String getCommentCreatorProfileImg(int userId) {
		return userController.getProfileImgPathById(userId);
	}

	public Comment[] getComments() {
		return comments;
	}

	public void setComments(Comment[] comments) {
		this.comments = comments;
	}

	public Website getWebsite() {
		return website;
	}

	public void setWebsite(Website website) {
		this.website = website;
	}

	public Comment getNewComment() {
		return newComment;
	}

	public void setNewComment(Comment newComment) {
		this.newComment = newComment;
	}

	public int getWebsiteId() {
		return websiteId;
	}

	public void setWebsiteId(int websiteId) {
		this.websiteId = websiteId;
	}

	public UserController getUserController() {
		return userController;
	}

	public void setUserController(UserController userController) {
		this.userController = userController;
	}

	public Comment getToBeDeletedComment() {
		return toBeDeletedComment;
	}

	public void setToBeDeletedComment(Comment toBeDeletedComment) {
		this.toBeDeletedComment = toBeDeletedComment;
	}

	public Comment getToBeEditedComment() {
		return toBeEditedComment;
	}

	public void setToBeEditedComment(Comment toBeEditedComment) {
		this.toBeEditedComment = toBeEditedComment;
	}

	public boolean isEditing() {
		return editing;
	}

	public void setEditing(boolean editing) {
		this.editing = editing;
	}

	public boolean isIdFound() {
		return idFound;
	}

	public void setIdFound(boolean idFound) {
		this.idFound = idFound;
	}

	public int getUserIdOfSelectedComment() {
		return userIdOfSelectedComment;
	}

	public void setUserIdOfSelectedComment(int userIdOfSelectedComment) {
		this.userIdOfSelectedComment = userIdOfSelectedComment;
	}

}
