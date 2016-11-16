package ch.bbc.rottengold.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import ch.bbc.rottengold.ejb.WebsiteInfoBeanLocal;
import ch.bbc.rottengold.model.Rating;
import ch.bbc.rottengold.model.Website;

@Named
@SessionScoped
public class WebsiteInfoController implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private WebsiteInfoBeanLocal websiteInfoBean;

	@Inject
	private Website currentWebsite;

	private Rating rating;

	@PostConstruct
	public void init() {
		setCurrentWebsite();
	}

	private void setCurrentWebsite() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		try {
			currentWebsite = websiteInfoBean.getWebsiteInfo((int) request.getAttribute("id"));
		} catch (NullPointerException exception) {
			currentWebsite = new Website("Default", "http://localhost:8080/RottenGold_WEB/faces/mainFrame.xhtml",
					"This is our Website");
		}
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

}
