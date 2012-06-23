package com.sfeir.common.gwt.client.place;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * Indicates the alias of a {@link PlaceTokenizer#getToken(Place)} is written to
 * {@link com.google.gwt.user.client.History#newItem}.
 * {@code com.google.gwt.place.rebind.PlaceHistoryMapperGenerator} looks
 * for this annotation on the factory methods that return a tokenizer, and on
 * the tokenizer types themselves.
 */
@Target({ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PrefixAlias {
	String[] value();
}