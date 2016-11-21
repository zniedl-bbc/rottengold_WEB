package ch.bbc.rottengold.controller;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ch.bbc.rottengold.ejb.AddWebsiteBeanLocal;
import ch.bbc.rottengold.model.Website;

@Named
@SessionScoped
public class AddWebsiteController implements Serializable {

	private static final long serialVersionUID = 1971055441635431099L;

	@EJB
	AddWebsiteBeanLocal addWebsiteBean;
	
	@Inject
	Website website;

	String errorMsg = "";
	Boolean failure = false;

	public String addWebsite() {
		boolean nameAllreadyExists = addWebsiteBean.getTitleByName(website);
		String checkedURL = checkURL(website.getUrl());
		website.setUrl(checkedURL);
		if (!nameAllreadyExists) {
			if (checkNameAndUrlEquality(website.getName(), checkedURL)) {
				if (!checkedURL.equals("Can not reach website")) {
					addWebsiteBean.addWebsite(website);
					System.out.println("You've done it");
				}
			}
		}
		return "checkedURL";
	}

	private boolean checkNameAndUrlEquality(String uncheckedname, String checkedURL) {
		String websiteNameFromUrl = "";
		int iterator = 0;
		boolean startForNameFiltering = false;
		while (iterator < checkedURL.length()) {
			char tmp = checkedURL.charAt(iterator);
			if (tmp == '.') {
				startForNameFiltering = switchFlag(startForNameFiltering);
				iterator++;
			}
			if (startForNameFiltering) {
				websiteNameFromUrl += checkedURL.charAt(iterator);

			}
			iterator++;
		}
		if (uncheckedname.toLowerCase().contains(websiteNameFromUrl.toLowerCase())) {
			return true;
		}
		return false;
	}

	private String checkURL(String uncheckedURL) {
		String url = "http://" + uncheckedURL;

		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("HEAD");
			int responseCode = connection.getResponseCode();

			if (responseCode != 200) {
				return "Can not reach website";
			}
		} catch (IOException e) {
			return "Can not reach website";
		}

		return url;
	}

	private boolean switchFlag(boolean startForNameFiltering) {
		if (startForNameFiltering) {
			return false;
		} else {
			return true;
		}
	}
	public Website getWebsite() {
		return website;
	}

	public void setWebsite(Website website) {
		this.website = website;
	}
}
