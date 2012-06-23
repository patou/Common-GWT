package com.sfeir.common.gwt.client.history;

import java.util.Map;

import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapperWithFactory;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.sfeir.common.gwt.client.place.PlaceProperty;
import com.sfeir.common.gwt.client.place.Tokenizer;


public interface PlaceHistoryMapper extends PlaceHistoryMapperWithFactory<Void> {
	/**
	 * Set a {@link ContantsWithLookup} interface wich translate the Prefix to the current locale language
	 * The translation is only used for the {@link Place} to String, you must add all translation with \@{@link PrefixAlias}
	 * @param i18n
	 */
	public void setTranslation(ConstantsWithLookup i18n);
	
	/**
	 * Get property map from {@link Place}
	 * @param place The {@link Place} 
	 * @return null of the place has no {@link PlaceTokenizer} and the {@link PlaceTokenizer} didn't extends {@link Tokenizer}
	 */
	public Map<String, PlaceProperty> getPlaceProperties(Place place);

	/**
	 * Get the {@link Tokenizer} or null if the {@link PlaceTokenizer} didn't extends {@link Tokenizer}
	 * @param prefix
	 * @return
	 */
	Tokenizer<?> getPlaceTokenizer(String prefix);

	/**
	 * Get the prefix from a Place
	 * @param place
	 * @return
	 */
	String getPrefix(Place place);
}
