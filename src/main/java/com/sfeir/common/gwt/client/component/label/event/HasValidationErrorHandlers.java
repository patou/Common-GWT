package com.sfeir.common.gwt.client.component.label.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasValidationErrorHandlers extends HasHandlers {
    public HandlerRegistration addValidationErrorHandler(ValidationErrorHandler handler);
}
