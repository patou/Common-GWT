package com.sfeir.common.gwt.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Events launched to set the title message
 * 
 * 
 */
public class SetTitleEvent extends GwtEvent<SetTitleEvent.Handler> {
	public static final Type<Handler> TYPE = new GwtEvent.Type<Handler>();
	private final String title;

	public SetTitleEvent(String titleMessage) {
		this.title = titleMessage;
	}

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	public static Type<Handler> getType() {
		return TYPE;
	}

	public String getTitle() {
		return title;
	}

	@Override
	protected void dispatch(SetTitleEvent.Handler handler) {
		handler.onSetTitle(this);
	}

	public interface Handler extends EventHandler {
		void onSetTitle(SetTitleEvent e);
	}
}
