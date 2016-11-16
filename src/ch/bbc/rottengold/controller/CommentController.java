package ch.bbc.rottengold.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
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

	private List<Comment> comments;

	private Comment defaultComment = new Comment("Default", "This is the default comment");
	
	private List<Comment> listForDefaultComment;

	private Website website;

	@PostConstruct
	public void init() {
//		setComments(getCommentsViaWebsite());
	}

	private List<Comment> getCommentsViaWebsite() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		if (request.getAttribute("id") != null) {
			return commentBean.getCommentsViaWebsite((int) request.getAttribute("id"));
		} else {
			listForDefaultComment = new ArrayList<Comment>(1);
			listForDefaultComment.add(defaultComment);
			return listForDefaultComment;
		}
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Website getWebsite() {
		return website;
	}

	public void setWebsite(Website website) {
		this.website = website;
	}

}
