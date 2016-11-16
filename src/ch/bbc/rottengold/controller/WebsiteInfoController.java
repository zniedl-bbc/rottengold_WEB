package ch.bbc.rottengold.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import ch.bbc.rottengold.ejb.WebsiteInfoBeanLocal;
import ch.bbc.rottengold.model.Website;

@Named
@SessionScoped
public class WebsiteInfoController implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EJB
	private WebsiteInfoBeanLocal websiteInfoBean;
	
	private Website currentWebsite;

	@PostConstruct
	public void init(){
		setCurrentWebsite();
	}

	private void setCurrentWebsite() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		currentWebsite = websiteInfoBean.getWebsiteInfo((int) request.getAttribute("id"));
	}
}
