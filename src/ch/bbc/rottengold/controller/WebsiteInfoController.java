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

	public void setCurrentWebsiteFromId() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String idURLParam = request.getParameter("id");
		currentWebsite = websiteInfoBean.getWebsiteInfo(idURLParam);

	}

	public Website getCurrentWebsite() {
		setCurrentWebsiteFromId();
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
