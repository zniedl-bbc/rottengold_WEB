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

	private Rating[] allRatingsForCurrentWebsite;

	@PostConstruct
	public void init() {
		currentWebsite = websiteInfoBean.getWebsiteInfo("" + commentController.getWebsiteId());
		setRating(ratingBean.getRatingForWebsiteWithUser(commentController.getWebsiteId(),
				commentController.getUserController().getUser().getId()));
		setAllRatingsForCurrentWebsite(ratingBean.getAllRatingsForWebsite(commentController.getWebsiteId()));
	}

	public double getAverageRating() {
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

	public boolean isIt1StarRating() {
		if (getAverageRating() > 0.5) {
			return true;
		}
		return false;
	}

	public boolean isIt2StarRating() {
		if (getAverageRating() > 1.5) {
			return true;
		}
		return false;
	}

	public boolean isIt3StarRating() {
		if (getAverageRating() > 2.5) {
			return true;
		}
		return false;
	}

	public boolean isIt4StarRating() {
		if (getAverageRating() > 3.5) {
			return true;
		}
		return false;
	}

	public boolean isIt5StarRating() {
		if (getAverageRating() > 4.5) {
			return true;
		}
		return false;
	}

	public void set1StarRatingForUser() {
		ratingBean.setNewRatingForThisUser(1, commentController.getUserController().getUser().getId(),
				commentController.getWebsiteId());
	}

	public void set2StarRatingForUser() {
		ratingBean.setNewRatingForThisUser(2, commentController.getUserController().getUser().getId(),
				commentController.getWebsiteId());
	}

	public void set3StarRatingForUser() {
		ratingBean.setNewRatingForThisUser(3, commentController.getUserController().getUser().getId(),
				commentController.getWebsiteId());
	}

	public void set4StarRatingForUser() {
		ratingBean.setNewRatingForThisUser(4, commentController.getUserController().getUser().getId(),
				commentController.getWebsiteId());
	}

	public void set5StarRatingForUser() {
		ratingBean.setNewRatingForThisUser(5, commentController.getUserController().getUser().getId(),
				commentController.getWebsiteId());
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

	public boolean isRatingAvailable() {
		return true;
	}

	public Rating[] getAllRatingsForCurrentWebsite() {
		return allRatingsForCurrentWebsite;
	}

	public void setAllRatingsForCurrentWebsite(Rating[] allRatingsForCurrentWebsite) {
		this.allRatingsForCurrentWebsite = allRatingsForCurrentWebsite;
	}

}
