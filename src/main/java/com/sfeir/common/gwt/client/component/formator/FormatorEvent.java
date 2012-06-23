package com.sfeir.common.gwt.client.component.formator;

import com.google.gwt.event.shared.GwtEvent;

public class FormatorEvent<T> extends GwtEvent<FormatorHandler<T>> {
    private static Type<FormatorHandler<?>> TYPE;
    private T value;

    public static <T> T format(HasFormatorHandlers<T> source, T value) {
        if (TYPE != null) {
            FormatorEvent<T> event = new FormatorEvent<T>(value);
            source.fireEvent(event);
            return event.getValue();
        }
        else {
            return value;
        }
    }

    public FormatorEvent(T value) {
        setValue(value);
    }

    @Override
    protected void dispatch(FormatorHandler<T> handler) {
        handler.format(this);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Type<FormatorHandler<T>> getAssociatedType() {
        return (Type) TYPE;
    }

    public static Type<FormatorHandler<?>> getType() {
        if (TYPE == null) {
            TYPE = new Type<FormatorHandler<?>>();
        }
        return TYPE;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

}
