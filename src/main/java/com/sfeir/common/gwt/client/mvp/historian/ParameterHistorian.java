package com.sfeir.common.gwt.client.mvp.historian;

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
				String path = URL.decode(Window.Location.getPath());
				token = formater.cleanTokenPath(path);
			}
		}
		GWT.log("ParameterHistorian.getToken() " + token);
		return token;
	}
}