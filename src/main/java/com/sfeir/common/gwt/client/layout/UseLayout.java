package com.sfeir.common.gwt.client.layout;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define the layout to use for this place
 * 
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UseLayout {
    Class<? extends AbstractLayoutPlace> value();
}