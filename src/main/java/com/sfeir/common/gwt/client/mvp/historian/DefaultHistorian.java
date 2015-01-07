package com.sfeir.common.gwt.client.mvp.historian;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.sfeir.common.gwt.client.mvp.PlaceHistoryHandler.Historian;

/**
 * Default implementation of {@link Historian}, based on {@link History}.
 */
public class DefaultHistorian implements Historian {
	public com.google.gwt.event.shared.HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> valueChangeHandler) {
		return History.addValueChangeHandler(valueChangeHandler);
	}

	public String getToken() {
		return History.getToken();
	}

	public void newItem(String token, boolean issueEvent) {
		History.newItem(token, issueEvent);
	}

	@Override
	public void replaceToken(String token, boolean issueEvent) {
		if (!getToken().equals(token))
            History.replaceItem(token, issueEvent);
	}
}