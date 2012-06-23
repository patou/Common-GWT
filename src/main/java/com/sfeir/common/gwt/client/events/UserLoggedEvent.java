package com.sfeir.common.gwt.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event launched when a new user is logged.
 * 
 */

public class UserLoggedEvent extends GwtEvent<UserLoggedEvent.Handler> {
	public static final Type<Handler> TYPE = new GwtEvent.Type<Handler>();
	private Object userdata;

	public UserLoggedEvent(Object userdata) {
		this.userdata = userdata;
	}

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	public static Type<Handler> getType() {
		return TYPE;
	}

	public Object getUserData() {
		return userdata;
	}

	@Override
	protected void dispatch(UserLoggedEvent.Handler handler) {
		handler.onUserLogged(this);
	}

	public interface Handler extends EventHandler {
		void onUserLogged(UserLoggedEvent e);
	}
}
