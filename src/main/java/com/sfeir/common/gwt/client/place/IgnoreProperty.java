package com.sfeir.common.gwt.client.place;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Ignore the property from the generated tokenizer
 * 
 */
@Target({ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreProperty {
}
