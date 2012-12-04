package com.sfeir.common.gwt.client.mvp.historian;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class HistorianTokenFormater {
	protected String cleanTokenPath(String path) {
		String token = "";
		if (path != null && !path.isEmpty() && path.startsWith("/")) {
			token = path.substring(1);
		}
		if (GWT.isClient() && !GWT.isProdMode()) { //GWT parameters
			token = token.replaceAll("[?&]gwt\\.codesvr=([:.0-9]*)", "");
		}
		return token;
	}

	protected String getTokenPath(String token) {
		String newUri = token;
		if (!newUri.startsWith("/"))
			newUri = "/" + newUri;
		int posQuestionMark = newUri.indexOf('?');
		if (GWT.isClient() && !GWT.isProdMode()) { //GWT parameters
			String gwtcodesvr = Window.Location.getParameter("gwt.codesvr");
			if (gwtcodesvr != null)
				newUri += ((posQuestionMark > 0) ? "&" : "?") + "gwt.codesvr=" + gwtcodesvr;
		}
		return newUri;
	}
}
