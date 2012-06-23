package com.sfeir.common.gwt.client.component.validator;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasValidatorHandlers<V> extends HasHandlers {
    public HandlerRegistration addValidator(ValidatorHandler<V> handler);
}
