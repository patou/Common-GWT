package com.sfeir.common.gwt.client.place;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to add to a Place type to ignore the Place from the Tokenizer interface
 * 
 */
@Target({ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnorePlace {

}
