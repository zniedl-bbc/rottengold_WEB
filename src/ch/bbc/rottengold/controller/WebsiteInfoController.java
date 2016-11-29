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

	private Rating rating;

	private boolean ratingAvailable;

	private Rating[] allRatingsForCurrentWebsite;

	@PostConstruct
	public void init() {
		currentWebsite = websiteInfoBean.getWebsiteInfo("" + commentController.getWebsiteId());
		setRating(ratingBean.getRatingForWebsiteWithUser(commentController.getWebsiteId(),
				commentController.getUserController().getUser().getId()));
		allRatingsForCurrentWebsite = ratingBean.getAllRatingsForWebsite(commentController.getWebsiteId());
	}

	private double getAverageRating() {
		int counter = 0;
		double averageRating = 0.0;

		try {
			for (Rating r : allRatingsForCurrentWebsite) {
				averageRating += r.getRating();
				counter++;
			}
		} catch (NullPointerException e) {

		}

		averageRating = averageRating / counter;

		return averageRating;
	}

	public void setStarRatingForUser(int rating) {
		ratingBean.setNewRatingForThisUser(rating, commentController.getUserController().getUser().getId(),
				currentWebsite.getId());
	}

	public Website getCurrentWebsite() {
		return currentWebsite;
	}

	public void setCurrentWebsite(Website currentWebsite) {
		this.currentWebsite = currentWebsite;
	}

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
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

}
