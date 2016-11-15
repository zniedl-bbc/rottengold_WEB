package ch.bbc.rottengold.controller;

import java.util.Collection;

import javax.ejb.EJB;

import ch.bbc.rottengold.ejb.CommentLister;
import ch.bbc.rottengold.model.Comment;

public class CommentListController {
	


	@EJB
	CommentLister commentLister;
	
	Collection<Comment> comments = getCommentsViaWebsite();

	private Collection<Comment> getCommentsViaWebsite() {
		return (Collection<Comment>) commentLister.getCommentsViaWebsite(getWebsiteFromSession());
	}

	private int getWebsiteFromSession(){
		return 1;
		
	}
}
