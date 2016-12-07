package ch.bbc.rottengold.controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import ch.bbc.rottengold.ejb.RatingBeanLocal;
import ch.bbc.rottengold.ejb.WebsiteInfoBeanLocal;
import ch.bbc.rottengold.model.Rating;
import ch.bbc.rottengold.model.Website;

@Named
@ViewScoped
public class WebsiteInfoController implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private WebsiteInfoBeanLocal websiteInfoBean;

	@EJB
	private RatingBeanLocal ratingBean;

	@Inject
	private Website currentWebsite;

	@Inject
	private CommentController commentController;

	private double averageRating;

	private Rating ratingFromUser;

	private boolean ratingAvailable;

	private Rating[] allRatingsForCurrentWebsite;

	/**
	 * This function is used to initialize the website info whenever this view
	 * is called
	 */
	@PostConstruct
	public void init() {
		if (commentController.getWebsiteId() == 0) {
			commentController.setWebsiteId(1);
		}
		currentWebsite = websiteInfoBean.getWebsiteInfo("" + commentController.getWebsiteId());
		ratingFromUser = ratingBean.getRatingForWebsiteWithUser(commentController.getWebsiteId(),
				commentController.getUserController().getUser().getId());
		allRatingsForCurrentWebsite = ratingBean.getAllRatingsForWebsite(commentController.getWebsiteId());
	}

	public double getAverageRating() {
		int counter = 0;
		averageRating = 0.0;

		try {
			for (Rating r : allRatingsForCurrentWebsite) {
				averageRating += r.getRating();
				counter++;
			}
		} catch (NullPointerException e) {

		}
		if (counter == 0) {
			counter = 1;
		}
		averageRating = averageRating / counter;

		return averageRating;
	}

	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}

	public String setStarRatingForUser(int rating) {
		if (ratingFromUser == null) {
			ratingBean.setNewRatingForThisUser(rating, commentController.getUserController().getUser().getId(),
					currentWebsite.getId());
		} else {
			ratingBean.updateRating(rating, commentController.getUserController().getUser().getId(),
					currentWebsite.getId());
		}
		return "mainFrame?faces-redirect=true&includeViewParams=true";
	}

	public Website getCurrentWebsite() {
		return currentWebsite;
	}

	public void setCurrentWebsite(Website currentWebsite) {
		this.currentWebsite = currentWebsite;
	}

	public void setRatingAvailable(boolean ratingAvailable) {
		this.ratingAvailable = ratingAvailable;
	}

	public boolean ratingAvailable(double requiredRating) {
		ratingAvailable = false;
		if (getAverageRating() > requiredRating) {
			ratingAvailable = true;
		}
		return ratingAvailable;
	}

	public Rating[] getAllRatingsForCurrentWebsite() {
		return allRatingsForCurrentWebsite;
	}

	public void setAllRatingsForCurrentWebsite(Rating[] allRatingsForCurrentWebsite) {
		this.allRatingsForCurrentWebsite = allRatingsForCurrentWebsite;
	}

	public Rating getRatingFromUser() {
		return ratingFromUser;
	}

	public void setRatingFromUser(Rating ratingFromUser) {
		this.ratingFromUser = ratingFromUser;
	}
}
