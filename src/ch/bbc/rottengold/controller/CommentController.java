package ch.bbc.rottengold.controller;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import ch.bbc.rottengold.ejb.CommentBeanLocal;
import ch.bbc.rottengold.model.Comment;
import ch.bbc.rottengold.model.Website;

@Named
@SessionScoped
public class CommentController implements Serializable {

	private static final long serialVersionUID = -2587101126657460690L;

	@EJB
	private CommentBeanLocal commentBean;

	@Inject
	private UserController userController;

	private int websiteId;

	private Comment[] comments;

	private int commentDeleteID;

	@Inject
	private Comment newComment;

	private Website website;

	private void setCommentsViaWebsiteId() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String idURLParam = request.getParameter("id");
		if (idURLParam != null) {
			setWebsiteId(Integer.parseInt(idURLParam));
		}
		comments = commentBean.getCommentsViaWebsite(idURLParam);
	}

	public String addNewComment() {
		newComment.setId_website(getWebsiteId());
		newComment.setId_user(getUserController().getUser().getId());
		commentBean.addComment(newComment);
		return "";

	}

	public String editComment() {
		int commentDeleteID = new Integer(
				FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("commentDeleteID"));
		System.out.println("Edit Comment");
		return "";
	}

	public String deleteComment() {
		int commentDeleteID = new Integer(
				FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("commentDeleteID"));
		commentBean.deleteComment(commentDeleteID);
		return "";
	}

	public Comment[] getComments() {
		setCommentsViaWebsiteId();
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

	public int getCommentDeleteID() {
		return commentDeleteID;
	}

	public void setCommentDeleteID(int commentDeleteID) {
		this.commentDeleteID = commentDeleteID;
	}

}
