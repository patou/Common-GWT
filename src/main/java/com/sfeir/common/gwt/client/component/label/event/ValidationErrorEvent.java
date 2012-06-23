package com.sfeir.common.gwt.client.component.label.event;

import java.util.List;

import com.google.gwt.event.shared.GwtEvent;

public class ValidationErrorEvent extends GwtEvent<ValidationErrorHandler> {
    private static final Type<ValidationErrorHandler> TYPE = new Type<ValidationErrorHandler>();
    private List<String> errors;
    
    public ValidationErrorEvent(List<String> errors) {
    	setErrors(errors);
    }
    
    @Override
    protected void dispatch(ValidationErrorHandler handler) {
        handler.onValidationError(this);
    }
    
    @Override
    public Type<ValidationErrorHandler> getAssociatedType() {
        return TYPE;
    }
    
    public static Type<ValidationErrorHandler> getType() {
        return TYPE;
    }

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public List<String> getErrors() {
		return errors;
	}    
}
