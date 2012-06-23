package com.sfeir.common.gwt.client.component.validator;

import com.google.gwt.event.shared.EventHandler;

public interface ValidatorHandler<V> extends EventHandler {
    public void validate(ValidatorEvent<V> event);
}
