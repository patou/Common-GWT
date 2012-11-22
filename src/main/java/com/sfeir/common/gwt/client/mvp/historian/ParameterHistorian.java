package com.sfeir.common.gwt.client.mvp.historian;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;

public class ParameterHistorian extends DefaultHistorian {
	@Override
	public String getToken() {
		String token = History.getToken();
		if (token.isEmpty()) {
			token = Window.Location.getParameter("token");
			if (token == null) {
				String path = URL.decode(Window.Location.getPath());
				token = cleanTokenPath(path);
			}
		}
		return token;
	}

	protected String cleanTokenPath(String path) {
		String token = "";
		if (path != null && !path.isEmpty() && path.startsWith("/")) {
			token = path.substring(1);
		}
		return token;
	}

	protected String getTokenPath(String token) {
		String newUri = token;
		if (!newUri.startsWith("/"))
			newUri = "/" + newUri;
		int posQuestionMark = newUri.indexOf('?');
		if (!GWT.isProdMode()) { //GWT parameters
			String gwtcodesvr = Window.Location.getParameter("gwt.codesvr");
			if (gwtcodesvr != null)
				newUri += ((posQuestionMark > 0) ? "&" : "?") + "gwt.codesvr=" + gwtcodesvr;
		}
		return newUri;
	}
}