package com.sfeir.common.gwt.client.place;

import com.sfeir.common.gwt.client.place.Tokenizer.PropertyType;

public class PlaceProperty {
	Boolean required;
	Boolean defaultToken;
	String name;
	PropertyType type;
	String typeName;
	String message;
	String alias;

	public PlaceProperty(Boolean required, Boolean defaultToken, String name, PropertyType type, String typeName, String message, String alias) {
		super();
		this.required = required;
		this.defaultToken = defaultToken;
		this.name = name;
		this.typeName = typeName;
		this.type = type;
		this.message = message;
		this.alias = alias;
	}

	/**
	 * The property name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Type of the properties : - Integer - String - Long - Float - Double - Date - ListString - Enum
	 * 
	 * @return
	 */
	public PropertyType getType() {
		return type;
	}
	
	/**
	 * The className of the type
	 * @return
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * Description of the use of property
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Say if the property is required
	 * 
	 * @return
	 */
	public Boolean isRequired() {
		return required;
	}

	/**
	 * Say if the property is the default Token
	 * 
	 * @return
	 */
	public Boolean isDefaultToken() {
		return defaultToken;
	}
	
	public String getAlias() {
		return alias;
	}
	
}