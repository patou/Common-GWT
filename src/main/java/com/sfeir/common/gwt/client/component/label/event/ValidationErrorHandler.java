package com.sfeir.common.gwt.client.component.label.event;

import com.google.gwt.event.shared.EventHandler;

public interface ValidationErrorHandler extends EventHandler {
    public void onValidationError(ValidationErrorEvent event);
}
