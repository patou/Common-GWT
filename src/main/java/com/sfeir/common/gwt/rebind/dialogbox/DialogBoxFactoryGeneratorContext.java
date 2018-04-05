package com.sfeir.common.gwt.rebind.dialogbox;

import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.sfeir.common.gwt.rebind.MostToLeastDerivedPlaceTypeComparator;
import com.google.gwt.place.shared.Place;
import com.sfeir.common.gwt.client.place.Dialog;

/**
 * 
 * 
 */
class DialogBoxFactoryGeneratorContext {
	static DialogBoxFactoryGeneratorContext create(TreeLogger logger, TypeOracle typeOracle, String interfaceName) throws UnableToCompleteException {
	JClassType placeType = requireType(typeOracle, Place.class);

	JClassType interfaceType = typeOracle.findType(interfaceName);
	if (interfaceType == null) {
	    logger.log(TreeLogger.ERROR, "Could not find requested typeName: " + interfaceName);
	    throw new UnableToCompleteException();
	}

	if (interfaceType.isInterface() == null) {
	    logger.log(TreeLogger.ERROR, interfaceType.getQualifiedSourceName() + " is not an interface.", null);
	    throw new UnableToCompleteException();
	}

	String implName = interfaceType.getName().replace(".", "_") + "Impl";

		return new DialogBoxFactoryGeneratorContext(logger, typeOracle, interfaceType, placeType, interfaceType.getPackage().getName(), implName);
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
	final JClassType placeType;

	final String implName;

	final String packageName;

	/**
	 * All place types and the prefix of their associated tokenizer, ordered from most-derived to least-derived type (and falling back to the natural ordering of their names).
	 */
	private TreeMap<JClassType, Dialog> dialogBoxPlaceInfos = new TreeMap<JClassType, Dialog>(new MostToLeastDerivedPlaceTypeComparator());

	DialogBoxFactoryGeneratorContext(TreeLogger logger, TypeOracle typeOracle, JClassType interfaceType, JClassType placeType, String packageName, String implName) {
		this.logger = logger;
		this.typeOracle = typeOracle;
		this.interfaceType = interfaceType;
		this.placeType = placeType;
		this.packageName = packageName;
		this.implName = implName;
	}

	public Map<JClassType, Dialog> getDialogBoxPlaceInfos() throws UnableToCompleteException {
		ensureInitialized();
		return dialogBoxPlaceInfos;
	}

	public JMethod getTokenizerGetter(String prefix) throws UnableToCompleteException {
		ensureInitialized();
		Object tokenizerGetter = dialogBoxPlaceInfos.get(prefix);
		if (tokenizerGetter instanceof JMethod) {
			return (JMethod) tokenizerGetter;
		}
		return null;
	}

	public JClassType getTokenizerType(String prefix) throws UnableToCompleteException {
		ensureInitialized();
		Object tokenizerType = dialogBoxPlaceInfos.get(prefix);
		if (tokenizerType instanceof JClassType) {
			return (JClassType) tokenizerType;
		}
		return null;
	}

	void ensureInitialized() throws UnableToCompleteException {
		if (dialogBoxPlaceInfos == null) {
			initDialogBoxPlaceInfosFromScan();
		}
	}

	private void initDialogBoxPlaceInfosFromScan() throws UnableToCompleteException {
		JClassType[] subtypes = placeType.getSubtypes();
		for (JClassType type : subtypes) {
			// IgnorePlace ignorePlace = tokenizerType.getAnnotation(IgnorePlace)
			if (!type.isAnnotationPresent(Dialog.class)) {
				continue;
			}
			Dialog annotation = type.getAnnotation(Dialog.class);
			dialogBoxPlaceInfos.put(type, annotation);
		}
	}
}