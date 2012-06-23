package com.sfeir.common.gwt.client.history;

import java.util.Map;
import java.util.MissingResourceException;

import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.place.impl.AbstractPlaceHistoryMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.sfeir.common.gwt.client.place.PlaceProperty;
import com.sfeir.common.gwt.client.place.Tokenizer;
import com.sfeir.common.gwt.client.utils.AsciiUtils;

/**
 * Abstract implementation of {@link PlaceHistoryMapper}.
 * 
 * @param <F>
 *            factory type
 */
public abstract class AbstractPlaceHistoryMapperExtend implements PlaceHistoryMapper {

	private static final char FRAGMENT = '!';
	private ConstantsWithLookup i18n = null;

	/**
	 * Return value for {@link AbstractPlaceHistoryMapper#getPrefixAndToken(Place)}.
	 */
	public static class PrefixAndToken {
		public final String prefix;
		public final String token;
		public final Boolean canBeIndexed;
		public Map<String, String> values;

		public PrefixAndToken(String prefix, String token, Boolean canBeIndexed, Map<String, String> values) {
			assert prefix != null && !prefix.contains(":");
			this.prefix = prefix;
			this.token = token;
			this.canBeIndexed = canBeIndexed;
			this.values = values;
		}

		public PrefixAndToken(String prefix, String token, Boolean canBeIndexed) {
			assert prefix != null && !prefix.contains(":");
			this.prefix = prefix;
			this.token = token;
			this.canBeIndexed = canBeIndexed;
		}

	}

	public Place getPlace(String token) {

		int colonAt = token.indexOf(':');
		int start = (token.charAt(0) == FRAGMENT) ? 1 : 0;
		String initial;
		String rest;
		if (colonAt >= 0) {
			initial = token.substring(start, colonAt);
			rest = token.substring(colonAt + 1);
		} else {
			initial = "";
			rest = token.substring(start);
		}
		PlaceTokenizer<?> tokenizer = getTokenizer(initial);
		if (tokenizer != null) {
			return tokenizer.getPlace(rest);
		}
		return null;
	}

	/**
	 * 
	 */
	public String getToken(Place place) {
		PrefixAndToken token = getPrefixAndToken(place);
		return toString(token);
	}
	
	@Override
	public Map<String, PlaceProperty> getPlaceProperties(Place place) {
		PrefixAndToken prefix = getPrefixAndToken(place);
		if (prefix == null || prefix.prefix == null)
			return null;
		PlaceTokenizer<?> tokenizer = getTokenizer(prefix.prefix);
		if (tokenizer != null && tokenizer instanceof Tokenizer<?>) {
			return ((Tokenizer<?>) tokenizer).getProperties();
		}
		return null;
	}

	/**
	 * To String of a PrefixAndToken
	 * @param token
	 * @return
	 */
	protected String toString(PrefixAndToken token) {
		StringBuilder sb = new StringBuilder();
		if (token != null) {
			if (token.canBeIndexed) {
				sb.append('!');
			}
			if (token.prefix.length() > 0) {
				sb.append(getTranslatedPrefix(token.prefix));
				sb.append(':');
			}
			sb.append(token.token);
		}
		return sb.toString();
	}

	public void setFactory(Void factory) {
	}

	/**
	 * Set the constantswithlookup for automatically translate the prefix
	 * @param i18n
	 */
	@Override
	public void setTranslation(ConstantsWithLookup i18n) {
		this.i18n = i18n;
	}

	/**
	 * Get translated prefix
	 * @param prefix
	 * @return
	 */
	public String getTranslatedPrefix(String prefix) {
		if (i18n != null) {
			try {
				String tr = i18n.getString(prefix);
				tr = AsciiUtils.clean(tr);
				return tr;
			}
			catch (MissingResourceException e) {
				//Return prefix if not found resource
			}
		}
		return prefix;
	}

	/**
	 * @param newPlace
	 *            what needs tokenizing
	 * @return the token, or null
	 */
	protected abstract PrefixAndToken getPrefixAndToken(Place newPlace);

	/**
	 * @param prefix
	 *            the prefix found on the history token
	 * @return the PlaceTokenizer registered with that token, or null
	 */
	protected abstract PlaceTokenizer<?> getTokenizer(String prefix);

	@Override
	public String getPrefix(Place place) {
		PrefixAndToken token = getPrefixAndToken(place);
		return (token != null) ? token.prefix : null;
	}
	
	@Override
	public Tokenizer<?> getPlaceTokenizer(String prefix) {
		PlaceTokenizer<?> tokenizer = getTokenizer(prefix);
		if (tokenizer != null && tokenizer instanceof Tokenizer) {
			return (Tokenizer<?>) tokenizer;
		}
		return null;
	}
}