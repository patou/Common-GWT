package com.sfeir.common.gwt.client.component.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class KeyEnterPressEvent extends GwtEvent<KeyEnterPressEvent.Handler> {
    private static final Type<Handler> TYPE = new Type<Handler>();
    
    @Override
    protected void dispatch(Handler handler) {
        handler.onKeyEnter(this);
    }
    
    @Override
    public Type<Handler> getAssociatedType() {
        return TYPE;
    }
    
    public static Type<Handler> getType() {
        return TYPE;
    }
    
    public interface Handler extends EventHandler {
	public void onKeyEnter(KeyEnterPressEvent e);
    }
}
