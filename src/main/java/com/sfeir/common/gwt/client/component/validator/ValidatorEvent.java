package com.sfeir.common.gwt.client.component.validator;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.GwtEvent;

public class ValidatorEvent<T> extends GwtEvent<ValidatorHandler<T>> {
    private static Type<ValidatorHandler<?>> TYPE;
    private T value;
    private List<String> errors = new ArrayList<String>();

    public static <T> ValidatorEvent<T> validate(HasValidatorHandlers<T> source, T value) {
        if (TYPE != null) {
            ValidatorEvent<T> event = new ValidatorEvent<T>(value);
            source.fireEvent(event);
            return event;
        } else {
            return null;
        }
    }

    public ValidatorEvent(T value) {
        setValue(value);
    }

    @Override
    protected void dispatch(ValidatorHandler<T> handler) {
        handler.validate(this);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Type<ValidatorHandler<T>> getAssociatedType() {
        return (Type) TYPE;
    }

    public static Type<ValidatorHandler<?>> getType() {
        if (TYPE == null) {
            TYPE = new Type<ValidatorHandler<?>>();
        }
        return TYPE;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public Boolean hasErrors() {
        return this.errors != null && this.errors.size() > 0;
    }
    
    public void clearErrors() {
        if (this.errors != null) {
            this.errors.clear();
        }
    }

    public void addError(String error) {
        if (this.errors == null)
            this.errors = new ArrayList<String>();
        if (error != null)
            this.errors.add(error);
    }

    public void setErrors(String error) {
        clearErrors();
        addError(error);
    }

    public void setErrors(List<String> errors) {
        clearErrors();
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

}
