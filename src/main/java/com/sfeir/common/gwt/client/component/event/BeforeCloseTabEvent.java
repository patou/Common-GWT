package com.sfeir.common.gwt.client.component.event;

import com.google.gwt.event.shared.GwtEvent;

public class BeforeCloseTabEvent extends GwtEvent<BeforeCloseTabHandler> {
    private static final Type<BeforeCloseTabHandler> TYPE = new Type<BeforeCloseTabHandler>();
    private int index = 0;
    private boolean canceled;
    
    public BeforeCloseTabEvent(int index) {
        setIndex(index);
    }
    
    @Override
    protected void dispatch(BeforeCloseTabHandler handler) {
        handler.onBeforeCloseTab(this);
    }
    
    @Override
    public Type<BeforeCloseTabHandler> getAssociatedType() {
        return TYPE;
    }
    
    public static Type<BeforeCloseTabHandler> getType() {
        return TYPE;
    }

    /**
     * Cancel the before selection event.
     * 
     * Classes overriding this method should still call super.cancel().
     */
    public void cancel() {
      setCanceled(true);
    }
    
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * The tab index to be closed
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     * 
     * @param canceled
     */
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public boolean isCanceled() {
        return canceled;
    }
    
}
