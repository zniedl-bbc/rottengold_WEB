package ch.bbc.rottengold.controller;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ch.bbc.rottengold.ejb.SearchBeanLocal;
import ch.bbc.rottengold.ejb.UserBeanLocal;
import ch.bbc.rottengold.model.Website;

@Named
@SessionScoped
public class SearchController {

	@Inject
	String searchInput;
	
	@EJB
	SearchBeanLocal searchBean;
	
	Website[] searchResults;

	public String searchWebsite() {
		searchResults = searchBean.searchWebsite(searchInput);
		return "/mainFrame";
	}

	public Website[] getSearchResults() {
		if (searchResults == null){
			searchResults = new Website[1];
			searchResults[0] = new Website();
		}
		return searchResults;
	}

	public void setSearchResults(Website[] searchResults) {
		this.searchResults = searchResults;
	}
}
