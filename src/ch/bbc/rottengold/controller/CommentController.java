package ch.bbc.rottengold.controller;

import java.io.Serializable;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import ch.bbc.rottengold.ejb.CommentBeanLocal;
import ch.bbc.rottengold.model.Comment;

@Named
@SessionScoped
public class CommentController implements Serializable {

	private static final long serialVersionUID = -2587101126657460690L;

	@EJB
	private CommentBeanLocal commentLister;

	private Collection<Comment> comments;

	@PostConstruct
	public void init() {
		setComments(getCommentsViaWebsite());
	}

	private Collection<Comment> getCommentsViaWebsite() {
		return (Collection<Comment>) commentLister.getCommentsViaWebsite(getWebsiteFromSession());
	}

	private int getWebsiteFromSession() {
		return 1;

	}

	public Collection<Comment> getComments() {
		return comments;
	}

	public void setComments(Collection<Comment> comments) {
		this.comments = comments;
	}

}
