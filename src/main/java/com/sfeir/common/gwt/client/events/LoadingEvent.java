package com.sfeir.common.gwt.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event launched when a loading state begin or finished, allow to automatically display an loading indicator
 * 
 *
 */
public class LoadingEvent extends GwtEvent<LoadingEvent.Handler> {
    public static final Type<Handler> TYPE = new GwtEvent.Type<Handler>();
    private final boolean loading;
    
    public LoadingEvent(boolean loading) {
	this.loading = loading;
    }
    
    @Override
    public Type<Handler> getAssociatedType() {
	return TYPE;
    }
    
    public static Type<Handler> getType() {
	return TYPE;
    }
    
    public boolean isLoading() {
	return loading;
    }

    @Override
    protected void dispatch(LoadingEvent.Handler handler) {
	handler.onLoading(this);
    }

    public interface Handler extends EventHandler {
	void onLoading(LoadingEvent e);
    }
}
