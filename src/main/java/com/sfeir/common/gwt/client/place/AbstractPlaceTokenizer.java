package com.sfeir.common.gwt.client.place;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.place.shared.Place;

/**
 * Abstract PlaceTokenizer
 * 
 * @param <P>
 */
public abstract class AbstractPlaceTokenizer<P extends Place> implements Tokenizer<P> {
	private static final String PARAMS_DELIMITER = "?";
	private static final String FIELD_DELIMITER = ";";
	private static final String LIST_STRING_DELIMITER = ",";
	private static final String FIELD_VALUE = "=";
	//used in generated code
	protected Tokenizer<? super P> parent = getParentTokenizer();

	@Override
	public P getPlace(String token) {
		Map<String, PlaceProperty> placeProperties = getProperties();
		Iterator<String> iterator = Splitter.on(PARAMS_DELIMITER).limit(2).split(token).iterator();
		String defaultToken = iterator.next();
		if (defaultToken != null && defaultToken.isEmpty())
			defaultToken = null;
		Map<String, String> properties = new HashMap<String, String>();
		String queryToken = null;
		if (iterator.hasNext()) {
			queryToken = iterator.next();
		} else if (defaultToken == null || defaultToken.contains(FIELD_VALUE)) {
			queryToken = defaultToken;
			defaultToken = null;
		}
		if (queryToken != null) {
			properties.putAll(Splitter.on(FIELD_DELIMITER).omitEmptyStrings().withKeyValueSeparator(FIELD_VALUE).split(queryToken));
		}
		for (PlaceProperty placeProp : placeProperties.values()) {
			if (placeProp.isDefaultToken() && defaultToken != null)
				properties.put(placeProp.getName(), unescapeString(defaultToken));
			if (placeProp.isRequired() && !properties.containsKey(placeProp.getName()))
				throw new RuntimeException("In the place " + getPlaceType().getName() + " the property " + placeProp.getName()
						+ " is required but was not present in the current token " + token);
		}
		return createPlaceWithProperties(properties);
	}

	public String getToken(P place) {
		StringBuilder sb = new StringBuilder();
		String defaultToken = null;
		Map<String, String> properties = getPlaceProperties(place);
		Map<String, PlaceProperty> placeProperties = getProperties();
		for (Entry<String, String> property : properties.entrySet()) {
			String value = property.getValue();
			if (value != null && placeProperties.get(property.getKey()).isDefaultToken() && defaultToken == null) {
				defaultToken = escapeString(value);
			} else {
				if ((value != null && !value.isEmpty()) || placeProperties.get(property.getKey()).isRequired()) {
					sb.append(FIELD_DELIMITER); // TODO choose the
					sb.append(property.getKey());
					sb.append(FIELD_VALUE); // TODO choose the
					sb.append(escapeString(value));
				}
			}
		}
		if (place instanceof ParametersPlace) {
            Map<String, String> parameters = ((ParametersPlace) place).getParameters();
            for (Entry<String, String> param : parameters.entrySet()) {
                if (placeProperties.get(param.getKey()) == null) {
                    sb.append(FIELD_DELIMITER);
                    sb.append(param.getKey());
                    sb.append(FIELD_VALUE);
                    sb.append(escapeString(param.getValue()));
                }
            }
        }
		if (defaultToken != null) {
			if (sb.length() > 0)
				sb.replace(0, 1, PARAMS_DELIMITER);
			sb.insert(0, defaultToken);
		} else {
			sb.replace(0, 1, "");
		}
		return sb.toString();
	}

	protected boolean mustAddProperty(Object property, Object defaultProperty) {
		return (!Objects.equal(property, defaultProperty));
	}
	
	/**
	 * Escape string with some reserved char (:,=,;)
	 * 
	 * @param value
	 * @return
	 */
	protected String escapeString(String value) {
		if (value == null)
			return "null";
		return value.replace(FIELD_VALUE, "%61").replace(FIELD_DELIMITER, "%59").replace(PARAMS_DELIMITER, "%3F");
	}

	/**
	 * Unescape string with some reserved char (:,=,;)
	 * 
	 * @param value
	 * @return
	 */
	protected String unescapeString(String value) {
		if (value == null || "null".equalsIgnoreCase(value))
			return null;
		return value.replace("%61", FIELD_VALUE).replace("%59", FIELD_DELIMITER).replace("%3F", PARAMS_DELIMITER);
	}
	
	@Override
	public Map<String,String> getPlaceProperties(P place) {
		Map<String, String> properties = new HashMap<String, String>();
		initPlaceProperties(place, properties);
		return properties;
	}
	
	@Override
	public Map<String, PlaceProperty> getProperties() {
		Map<String, PlaceProperty> properties = new HashMap<String, PlaceProperty>();
		buildProperties(properties);
		return properties;
	}
	
	public P createPlaceWithProperties(Map<String, String> properties) {
		P place = createPlace();
		initPlaceWithProperties(place, properties);
		if (place instanceof ParametersPlace) {
		    ((ParametersPlace) place).setParameters(properties);
		}
		return place;
	}
	
	protected Tokenizer<? super P> getParent() {
		return parent;
	}

	/*----------------------------------------------------------------------------------------------------------------------------------------------------
	 * Used by the generated code
	 * 
	 */

	protected Integer parseInteger(String value) {
		try {
			if (isNull(value))
				return null;
			return Integer.valueOf(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	protected Double parseDouble(String value) {
		try {
			if (isNull(value))
				return null;
			return Double.valueOf(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	protected Long parseLong(String value) {
		try {
			if (isNull(value))
				return null;
			return Long.valueOf(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	protected Float parseFloat(String value) {
		try {
			if (isNull(value))
				return null;
			return Float.valueOf(value);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	protected Boolean parseBoolean(String value) {
		try {
			if (isNull(value))
				return null;
			return Boolean.valueOf(value);
		} catch (Exception e) {
			return null;
		}
	}

	protected String parseString(String value) {
		if (isNull(value))
			return null;
		return unescapeString(value);
	}
	
	protected <E extends Enum<E>> E parseEnum(String value, Class<E> enumclass) {
		if (isNull(value))
			return null;
		try {
			return Enum.valueOf(enumclass, value);
		}
		catch (Exception e) {
			return null;
		}
	}

	private boolean isNull(String value) {
		return value == null || value.isEmpty() || "null".equalsIgnoreCase(value);
	}

	protected List<String> parseListString(String value) {
		if (isNull(value))
			return null;
		ArrayList<String> list = new ArrayList<String>();
		Iterable<String> interator = Splitter.on(LIST_STRING_DELIMITER).omitEmptyStrings().split(unescapeString(value));
		for (String string : interator) {
			if ("null".equalsIgnoreCase(string))
				list.add(null);
			else
				list.add(string.replace("%44", ","));
		}
		return list;
	}

	protected String toStringListString(List<String> value) {
		if (value == null || value.isEmpty())
			return null;
		ArrayList<String> list = new ArrayList<String>();
		for (String string : value) {
			if (string != null)
				list.add(string.replace(",", "%44"));
			else
				list.add("null");
		}
		return Joiner.on(LIST_STRING_DELIMITER).join(list);
	}

	protected String toString(Object value) {
		if (value == null)
			return null;
		return value.toString();
	}

	protected String toString(int value) {
		return Integer.toString(value);
	}

	protected String toString(double value) {
		return Double.toString(value);
	}

	protected String toString(float value) {
		return Float.toString(value);
	}

	protected String toString(long value) {
		return Long.toString(value);
	}

	protected String toString(boolean value) {
		return Boolean.toString(value);
	}
	
	protected <E extends Enum<E>> String toString(Enum<E> value) {
		if (value == null)
			return null;
		return value.name();
	}

	protected Date parseDate(String value) {
		try {
			if (isNull(value))
				return null;
			return getFormat().parse(value);
		} catch (Exception e) {
			return null;
		}
	}

	protected String toStringDate(Date value) {
		try {
			if (value == null)
				return null;
			return getFormat().format(value);
		} catch (Exception e) {
			return null;
		}
	}

	static DateTimeFormat format;

	protected DateTimeFormat getFormat() {
		if (format == null) {
			format = DateTimeFormat.getFormat("yyyy-MM-dd");
		}
		return format;
	}
}
