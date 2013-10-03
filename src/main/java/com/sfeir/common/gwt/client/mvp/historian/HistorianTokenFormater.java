package com.sfeir.common.gwt.client.mvp.historian;

import static com.google.common.base.Strings.isNullOrEmpty;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class HistorianTokenFormater {
	public String cleanTokenPath(String path) {
		String token = "";
		if (!isNullOrEmpty(path)) {
			token = path.startsWith("/") ? path.substring(1) : path;
		}
		if (GWT.isClient() && !GWT.isProdMode()) { //GWT parameters
			token = token.replaceAll("[?&]gwt\\.codesvr=([:.0-9]*)", "");
		}
		return token;
	}

	public String getTokenPath(String token) {
		String newUri = token;
		if (!newUri.startsWith("/"))
			newUri = "/" + newUri;
		int posQuestionMark = newUri.indexOf('?');
		if (GWT.isClient() && !GWT.isProdMode()) { //GWT parameters
			String gwtcodesvr = Window.Location.getParameter("gwt.codesvr");
			if (gwtcodesvr != null && !newUri.contains("gwt.codesvr"))
				newUri += ((posQuestionMark > 0) ? "&" : "?") + "gwt.codesvr=" + gwtcodesvr;
		}
		return newUri;
	}
}
