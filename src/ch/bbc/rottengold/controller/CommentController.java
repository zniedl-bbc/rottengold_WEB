package ch.bbc.rottengold.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import ch.bbc.rottengold.ejb.CommentBeanLocal;
import ch.bbc.rottengold.model.Comment;
import ch.bbc.rottengold.model.Website;

@Named
@ViewScoped
public class CommentController implements Serializable {

	private static final long serialVersionUID = -2587101126657460690L;

	@EJB
	private CommentBeanLocal commentBean;

	@Inject
	private UserController userController;
	
	private int websiteId;

	private Comment[] comments;

	private Comment toBeDeletedComment;

	private Comment toBeEditedComment;
	
	@Inject
	private Comment newComment;

	private Website website;
	
	private boolean editing;

	@PostConstruct
	public void init() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String idURLParam = request.getParameter("id");
		if (idURLParam != null) {
			setWebsiteId(Integer.parseInt(idURLParam));
		}
		setComments(commentBean.getCommentsViaWebsite(idURLParam));
		editing = false;
	}

	public boolean isUserCommentWriter(Comment comment){
		if (userController.getUser().getId() == comment.getId_user()){
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

	public void changeToEditMode(){
		editing=true;
	}
	
	public String editComment() {
		commentBean.editComment(toBeEditedComment);
		editing=false;
		return "mainFrame?faces-redirect=true&includeViewParams=true";
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

}
