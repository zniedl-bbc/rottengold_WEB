package ch.bbc.rottengold.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotFoundException;

import ch.bbc.rottengold.ejb.CommentBeanLocal;
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

	@Inject
	private UserController userController;

	private int websiteId;

	private Comment[] comments;

	private Comment toBeDeletedComment;

	private Comment toBeEditedComment;

	private boolean idFound;

	@Inject
	private Comment newComment;

	private Website website;

	private boolean editing;

	@PostConstruct
	public void init() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		int idURLParam = 0;
		idFound = false;
		if (request.getParameter("id") != null) {
			idURLParam = Integer.parseInt(request.getParameter("id"));
		} else if (request.getParameter("id") == "0") {
			idURLParam = 1;
		}
		setWebsiteId(idURLParam);
		comments = null;
		setComments(commentBean.getCommentsViaWebsite(idURLParam, websiteInfoBean.findBiggestWebsiteId(idURLParam)));
		if (comments != null) {
			idFound = true;
			if (comments[0].getId_website() != websiteId) {
				comments = null;
			}
		}

		editing = false;
	}

	public boolean isUserCommentWriter(Comment comment) {
		if (userController.getUser().getId() == comment.getId_user()) {
			return true;
		}

		return false;
	}

	public String addNewComment() {
		newComment.setId_website(getWebsiteId());
		newComment.setId_user(getUserController().getUser().getId());
		commentBean.addComment(newComment);
		newComment.clear();
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

	public String deleteComment() {
		commentBean.deleteComment(toBeDeletedComment.getId());
		return "mainFrame?faces-redirect=true&includeViewParams=true";
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

}
