package com.sfeir.common.gwt.client.component.formator;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasFormatorHandlers<V> extends HasHandlers {
    public HandlerRegistration addFormator(FormatorHandler<V> handler);
}
