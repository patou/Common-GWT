package com.sfeir.common.gwt.client.mvp.historian;

import static com.google.common.base.Strings.isNullOrEmpty;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;

public class ParameterHistorian extends DefaultHistorian {
	HistorianTokenFormater formater = GWT.create(HistorianTokenFormater.class);
	@Override
	public String getToken() {
		String token = History.getToken();
		if (token.isEmpty()) {
			token = Window.Location.getParameter("token");
			if (token == null) {
				String path = URL.decode(getUrl());
				token = formater.cleanTokenPath(path);
			}
		}
		GWT.log("ParameterHistorian.getToken() " + token);
		return token;
	}
	
	private String getUrl() {
		String path = Window.Location.getPath();
		String query = Window.Location.getQueryString();
		if (!isNullOrEmpty(query))
			path += "?" + query;
		return path;
	}
}