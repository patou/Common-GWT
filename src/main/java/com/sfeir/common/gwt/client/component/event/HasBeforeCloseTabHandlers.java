package com.sfeir.common.gwt.client.component.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasBeforeCloseTabHandlers extends HasHandlers {
    public HandlerRegistration addBeforeCloseTabHandler(BeforeCloseTabHandler handler);
}
