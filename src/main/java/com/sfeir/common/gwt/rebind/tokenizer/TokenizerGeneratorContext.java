package com.sfeir.common.gwt.rebind.tokenizer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.sfeir.common.gwt.client.place.IgnoreProperty;
import com.sfeir.common.gwt.client.place.PlaceProperty;
import com.sfeir.common.gwt.client.place.Property;
import com.sfeir.common.gwt.client.place.Tokenizer;
import com.sfeir.common.gwt.client.place.Tokenizer.PropertyType;

/**
 * This class is modified from the GWT class
 * 
 */
class TokenizerGeneratorContext {
	static TokenizerGeneratorContext create(TreeLogger logger, TypeOracle typeOracle, String interfaceName) throws UnableToCompleteException {
		JClassType tokenizerType = requireType(typeOracle, Tokenizer.class);
		JClassType placeType;

		JClassType interfaceType = typeOracle.findType(interfaceName);
		if (interfaceType == null) {
			logger.log(TreeLogger.ERROR, "Could not find requested typeName: " + interfaceName);
			throw new UnableToCompleteException();
		}

		if (interfaceType.isInterface() == null) {
			logger.log(TreeLogger.ERROR, interfaceType.getQualifiedSourceName() + " is not an interface.", null);
			throw new UnableToCompleteException();
		}

		placeType = findPlaceType(tokenizerType, interfaceType);
		if (placeType == null) {
			logger.log(TreeLogger.ERROR, interfaceType.getQualifiedSourceName() + " has no paramerized Place argument.", null);
			throw new UnableToCompleteException();
		}

		if (!placeType.isDefaultInstantiable()) {
			logger.log(
					Type.WARN,
					String.format("The place '%s' isn't instantiable (must be a class, have a default constructor, be a top level class or a static nested class)",
							placeType.getQualifiedSourceName()));
			throw new UnableToCompleteException();
		}

		String implName = placeType.getName().replace(".", "_") + "_TokenizerImpl";

		return new TokenizerGeneratorContext(logger, typeOracle, interfaceType, placeType, tokenizerType, placeType.getPackage().getName(), implName);
	}

	private static JClassType findPlaceType(JClassType tokenizerType, JClassType interfaceType) {
		JClassType superInterfaces[] = interfaceType.getImplementedInterfaces();

		for (JClassType superInterface : superInterfaces) {
			JParameterizedType parameterizedType = superInterface.isParameterized();
			if (parameterizedType != null && parameterizedType.getBaseType().equals(tokenizerType)) {
				return parameterizedType.getTypeArgs()[0];
			}
		}

		return null;
	}

	private static JClassType requireType(TypeOracle typeOracle, Class<?> clazz) {
		try {
			return typeOracle.getType(clazz.getName());
		} catch (NotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	final TreeLogger logger;
	final TypeOracle typeOracle;
	final JClassType interfaceType;

	final String implName;

	final String packageName;

	private final JClassType placeType;

	TokenizerGeneratorContext(TreeLogger logger, TypeOracle typeOracle, JClassType interfaceType, JClassType placeType, JClassType tokenizerType, String packageName,
			String implName) {
		this.logger = logger;
		this.typeOracle = typeOracle;
		this.interfaceType = interfaceType;
		this.placeType = placeType;
		this.packageName = packageName;
		this.implName = implName;
	}

	public JClassType getPlace() throws UnableToCompleteException {
		ensureInitialized();
		return placeType;
	}

	private List<PlaceProperty> placeProperties;

	public List<PlaceProperty> getPlaceProperties() {
		return placeProperties;
	}

	void ensureInitialized() throws UnableToCompleteException {
		if (placeProperties == null) {
			placeProperties = new ArrayList<PlaceProperty>();
			initPlaceProperties();
		}
	}

	private void initPlaceProperties() throws UnableToCompleteException {
		JField[] fields = placeType.getFields();
		String defaultTokenField = null;
		for (JField field : fields) {
			if (field.isAnnotationPresent(IgnoreProperty.class)) {
				continue;
			}
			Property annotation = field.getAnnotation(Property.class);
			PropertyType fieldType = getFieldType(field);
			if (fieldType == null) {
				logger.log(
						annotation == null ? TreeLogger.ERROR : TreeLogger.WARN,
						String.format(
								"The type %s of the field %s in the place %s isn't allowed by the tokenizer (use Integer, String, Long, Float, Double, Date, List<String> or create your own Tokenizer)",
								field.getType().getSimpleSourceName(), field.getName(), placeType.getName()));
				continue;
			}
			boolean required = false;
			boolean defaultToken = false;
			String message = "";
			if (annotation != null) {
				required = annotation.required();
				if (annotation.defaultToken()) {
					if (defaultTokenField != null) {
						logger.log(TreeLogger.ERROR,
								String.format("There are two defaultToken in the place %s with the field %s and %s.", placeType.getName(), field.getName(), defaultTokenField));
						throw new UnableToCompleteException();
					}
					defaultToken = true;
					defaultTokenField = field.getName();
				}
				message = annotation.value();
			}
			PlaceProperty property = new PlaceProperty(required, defaultToken, field.getName(), fieldType, field.getType().getQualifiedSourceName(), message);
			logger.log(TreeLogger.TRACE, String.format("%s.%s<%s>", placeType.getName(), field.getName(), fieldType.name()));
			placeProperties.add(property);
		}
	}

	JClassType stringClass;
	JClassType integerClass;
	JClassType longClass;
	JClassType dateClass;
	JClassType doubleClass;
	JClassType floatClass;
	JClassType booleanClass;
	JClassType listClass;

	// JClassType listStringClass;

	private PropertyType getFieldType(JField field) throws UnableToCompleteException {
		initTypeJClassType();
		JType type = field.getType();
		// Type primitif :
		if (type.isPrimitive() != null) {
			if (type.equals(JPrimitiveType.BOOLEAN))
				return PropertyType.BOOLEAN;
			if (type.equals(JPrimitiveType.INT))
				return PropertyType.INTEGER;
			if (type.equals(JPrimitiveType.FLOAT))
				return PropertyType.FLOAT;
			if (type.equals(JPrimitiveType.DOUBLE))
				return PropertyType.DOUBLE;
			if (type.equals(JPrimitiveType.LONG))
				return PropertyType.LONG;
		}
		// autres types
		else {
			JClassType classType = type.isClassOrInterface();
			if (classType != null) {
				if (classType.equals(booleanClass))
					return PropertyType.BOOLEAN;
				if (classType.equals(integerClass))
					return PropertyType.INTEGER;
				if (classType.equals(floatClass))
					return PropertyType.FLOAT;
				if (classType.equals(doubleClass))
					return PropertyType.DOUBLE;
				if (classType.equals(longClass))
					return PropertyType.LONG;
				if (classType.equals(stringClass))
					return PropertyType.STRING;
				if (classType.equals(dateClass))
					return PropertyType.DATE;
				if (classType.isAssignableTo(listClass) && isForListInterfaces(classType, stringClass)) {
					return PropertyType.LISTSTRING;
				}
				if (classType.isEnum() != null)
					return PropertyType.ENUM;
			}
		}
		return null;
	}

	private Boolean isForListInterfaces(JClassType type, JClassType listItemType) {
		JClassType rtn = null;
		JParameterizedType parameterizedType = type.isParameterized();
		if (parameterizedType != null && listClass.equals(parameterizedType.getBaseType())) {
			return parameterizedType.getTypeArgs()[0].equals(listItemType);
		}
		JClassType[] interfaces = type.getImplementedInterfaces();
		for (JClassType i : interfaces) {
			parameterizedType = i.isParameterized();
			if (parameterizedType != null && listClass.equals(parameterizedType.getBaseType())) {
				rtn = parameterizedType.getTypeArgs()[0];
			}
		}
		return rtn.equals(listItemType);
	}

	private void initTypeJClassType() {
		if (stringClass == null) {
			stringClass = requireType(typeOracle, String.class);
			integerClass = requireType(typeOracle, Integer.class);
			longClass = requireType(typeOracle, Long.class);
			dateClass = requireType(typeOracle, Date.class);
			doubleClass = requireType(typeOracle, Double.class);
			floatClass = requireType(typeOracle, Float.class);
			listClass = requireType(typeOracle, List.class);
			booleanClass = requireType(typeOracle, Boolean.class);
		}
	}
}