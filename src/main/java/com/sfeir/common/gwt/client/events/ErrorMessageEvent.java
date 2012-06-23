package com.sfeir.common.gwt.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Events launched to display an error message
 * 
 *
 */
public class ErrorMessageEvent extends GwtEvent<ErrorMessageEvent.Handler> {
    public static final Type<Handler> TYPE = new GwtEvent.Type<Handler>();
    private final String errorMessage;
    
    public ErrorMessageEvent(String errorMessage) {
	this.errorMessage = errorMessage;
    }
    
    @Override
    public Type<Handler> getAssociatedType() {
	return TYPE;
    }
    
    public static Type<Handler> getType() {
	return TYPE;
    }

    public String getErrorMessage() {
	return errorMessage;
    }
    
    @Override
    protected void dispatch(ErrorMessageEvent.Handler handler) {
	handler.onErrorMessage(this);
    }

    public interface Handler extends EventHandler {
	void onErrorMessage(ErrorMessageEvent e);
    }
}
