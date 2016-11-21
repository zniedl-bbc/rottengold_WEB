package ch.bbc.rottengold.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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

	private Comment[] comments;

	private Website website;


	private void setCommentsViaWebsiteId() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest(); 
		String idURLParam = request.getParameter("id");
		comments = commentBean.getCommentsViaWebsite(idURLParam);
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

}
