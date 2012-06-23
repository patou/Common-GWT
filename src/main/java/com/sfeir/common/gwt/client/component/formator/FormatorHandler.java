package com.sfeir.common.gwt.client.component.formator;

import com.google.gwt.event.shared.EventHandler;

public interface FormatorHandler<V> extends EventHandler {
    public void format(FormatorEvent<V> event);
}
