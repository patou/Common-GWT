package com.sfeir.common.gwt.rebind.place;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.place.rebind.MostToLeastDerivedPlaceTypeComparator;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapperWithFactory;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.google.gwt.place.shared.WithTokenizers;
import com.sfeir.common.gwt.client.layout.AbstractLayoutPlace;
import com.sfeir.common.gwt.client.place.IgnorePlace;
import com.sfeir.common.gwt.client.place.PrefixAlias;
import com.sfeir.common.gwt.client.place.Tokenizer;

/**
 * This class is modified from the GWT class
 * 
 * <p>
 * <span style="color:red">Experimental API: This class is still under rapid development, and is very likely to be deleted. Use it at your own risk. </span>
 * </p>
 */
class PlaceHistoryGeneratorContext {
	static PlaceHistoryGeneratorContext create(TreeLogger logger, TypeOracle typeOracle, String interfaceName) throws UnableToCompleteException {
	JClassType stringType = requireType(typeOracle, String.class);
	JClassType placeTokenizerType = requireType(typeOracle, PlaceTokenizer.class);
	JClassType tokenizerType = requireType(typeOracle, Tokenizer.class);
	JClassType placeType = requireType(typeOracle, Place.class);
	JClassType abstractLayoutType = requireType(typeOracle, AbstractLayoutPlace.class);
	JClassType placeHistoryMapperWithFactoryType = requireType(typeOracle, PlaceHistoryMapperWithFactory.class);

	JClassType factoryType;

	JClassType interfaceType = typeOracle.findType(interfaceName);
	if (interfaceType == null) {
	    logger.log(TreeLogger.ERROR, "Could not find requested typeName: " + interfaceName);
	    throw new UnableToCompleteException();
	}

	if (interfaceType.isInterface() == null) {
	    logger.log(TreeLogger.ERROR, interfaceType.getQualifiedSourceName() + " is not an interface.", null);
	    throw new UnableToCompleteException();
	}

	factoryType = findFactoryType(placeHistoryMapperWithFactoryType, interfaceType);

	String implName = interfaceType.getName().replace(".", "_") + "Impl";

	return new PlaceHistoryGeneratorContext(logger, typeOracle, interfaceType, factoryType, stringType, placeTokenizerType, tokenizerType, placeType, abstractLayoutType,
		interfaceType.getPackage().getName(), implName);
    }

	private static JClassType findFactoryType(JClassType placeHistoryMapperWithFactoryType, JClassType interfaceType) {
		JClassType superInterfaces[] = interfaceType.getImplementedInterfaces();

		for (JClassType superInterface : superInterfaces) {
			JParameterizedType parameterizedType = superInterface.isParameterized();
			if (parameterizedType != null && parameterizedType.getBaseType().equals(placeHistoryMapperWithFactoryType)) {
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

	final JClassType stringType;

	final JClassType placeTokenizerType;

	final TreeLogger logger;
	final TypeOracle typeOracle;
	final JClassType interfaceType;
	final JClassType tokenizerType;
	final JClassType factoryType;
	final JClassType abstractLayoutType;
	final JClassType placeType;

	final String implName;

	final String packageName;

	/**
	 * All tokenizers, either as a {@link JMethod} for factory getters or as a {@link JClassType} for types that must be GWT.create()d, by prefix.
	 */
	private HashMap<String, Object> tokenizers;

	/**
	 * All place types and the prefix of their associated tokenizer, ordered from most-derived to least-derived type (and falling back to the natural ordering of their names).
	 */
	private TreeMap<JClassType, String> placeTypes = new TreeMap<JClassType, String>(new MostToLeastDerivedPlaceTypeComparator());

	PlaceHistoryGeneratorContext(TreeLogger logger, TypeOracle typeOracle, JClassType interfaceType, JClassType factoryType, JClassType stringType, JClassType placeTokenizerType,
			JClassType tokenizerType, JClassType placeType, JClassType abstractLayoutType, String packageName, String implName) {
		this.logger = logger;
		this.typeOracle = typeOracle;
		this.interfaceType = interfaceType;
		this.factoryType = factoryType;
		this.stringType = stringType;
		this.placeTokenizerType = placeTokenizerType;
		this.placeType = placeType;
		this.tokenizerType = tokenizerType;
		this.abstractLayoutType = abstractLayoutType;
		this.packageName = packageName;
		this.implName = implName;
	}

	public Set<JClassType> getPlaceTypes() throws UnableToCompleteException {
		ensureInitialized();
		return placeTypes.keySet();
	}

	public String getPrefix(JClassType placeType) throws UnableToCompleteException {
		ensureInitialized();
		return placeTypes.get(placeType);
	}

	public Set<String> getPrefixes() throws UnableToCompleteException {
		ensureInitialized();
		return tokenizers.keySet();
	}

	public JMethod getTokenizerGetter(String prefix) throws UnableToCompleteException {
		ensureInitialized();
		Object tokenizerGetter = tokenizers.get(prefix);
		if (tokenizerGetter instanceof JMethod) {
			return (JMethod) tokenizerGetter;
		}
		return null;
	}

	public JClassType getTokenizerType(String prefix) throws UnableToCompleteException {
		ensureInitialized();
		Object tokenizerType = tokenizers.get(prefix);
		if (tokenizerType instanceof JClassType) {
			return (JClassType) tokenizerType;
		}
		return null;
	}

	void ensureInitialized() throws UnableToCompleteException {
		if (tokenizers == null) {
			assert placeTypes.isEmpty();
			tokenizers = new HashMap<String, Object>();
			initTokenizerGetters();
			initTokenizersWithoutGetters();
			initTokenizersFromScan();
			checkNotTokenizerPlace();
		}
	}

	private void checkNotTokenizerPlace() {
		JClassType[] subtypes = placeType.getSubtypes();
		for (JClassType type : subtypes) {
			if (!placeTypes.containsKey(type) && !type.isAnnotationPresent(IgnorePlace.class) && !type.isAbstract() && !type.isAssignableTo(abstractLayoutType)) {
				logger.log(Type.WARN, String.format("The place '%s' has no Tokenizer (create the Tokenizer or add the annotation @IgnorePlace)", type.getQualifiedSourceName()));
			}
		}
	}

	private void addPlaceTokenizer(Object tokenizerClassOrGetter, String prefix, JClassType tokenizerType) throws UnableToCompleteException {
		if (prefix.contains(":")) {
			logger.log(TreeLogger.ERROR, String.format("Found place prefix \"%s\" containing separator char \":\", on %s", prefix, getLogMessage(tokenizerClassOrGetter)));
			throw new UnableToCompleteException();
		}
		if (tokenizers.containsKey(prefix)) {
			logger.log(
					TreeLogger.ERROR,
					String.format("Found duplicate place prefix \"%s\" on %s, already seen on %s", prefix, getLogMessage(tokenizerClassOrGetter),
							getLogMessage(tokenizers.get(prefix))));
			throw new UnableToCompleteException();
		}
		JClassType placeType = getPlaceTypeForTokenizerType(tokenizerType);
		if (placeTypes.containsKey(placeType)) {
			logger.log(TreeLogger.ERROR, String.format("Found duplicate tokenizer's place type \"%s\" on %s, already seen on %s", placeType.getQualifiedSourceName(),
					getLogMessage(tokenizerClassOrGetter), getLogMessage(tokenizers.get(placeTypes.get(placeType)))));
			throw new UnableToCompleteException();
		}
		if (tokenizerClassOrGetter instanceof JClassType) {
			String[] alias = getAliasForTokenizerType((JClassType) tokenizerClassOrGetter);
			if (alias != null)
				for (String prefixAlias : alias) {
					tokenizers.put(prefixAlias, tokenizerClassOrGetter);
				}
		}
		tokenizers.put(prefix, tokenizerClassOrGetter);
		placeTypes.put(placeType, prefix);
	}

	private String getLogMessage(Object methodOrClass) {
		if (methodOrClass instanceof JMethod) {
			JMethod method = (JMethod) methodOrClass;
			return method.getEnclosingType().getQualifiedSourceName() + "#" + method.getName() + "()";
		}
		JClassType classType = (JClassType) methodOrClass;
		return classType.getQualifiedSourceName();
	}

	private JClassType getPlaceTypeForTokenizerType(JClassType tokenizerType) throws UnableToCompleteException {

		List<JClassType> implementedInterfaces = new ArrayList<JClassType>();

		JClassType isInterface = tokenizerType.isInterface();
		if (isInterface != null) {
			implementedInterfaces.add(isInterface);
		}

		implementedInterfaces.addAll(Arrays.asList(tokenizerType.getImplementedInterfaces()));

		JClassType rtn = placeTypeForInterfaces(implementedInterfaces);
		if (rtn == null) {
			logger.log(TreeLogger.ERROR, "Found no Place type for " + tokenizerType.getQualifiedSourceName());
			throw new UnableToCompleteException();
		}

		return rtn;
	}

	private String getPrefixForTokenizerGetter(JMethod method) throws UnableToCompleteException {
		Prefix annotation = method.getAnnotation(Prefix.class);
		if (annotation != null) {
			return annotation.value();
		}

		JClassType returnType = method.getReturnType().isClassOrInterface();
		return getPrefixForTokenizerType(returnType);
	}

	private String getPrefixForTokenizerType(JClassType returnType) throws UnableToCompleteException {
		Prefix annotation;
		annotation = returnType.getAnnotation(Prefix.class);
		if (annotation != null) {
			return annotation.value();
		}

		return getPlaceTypeForTokenizerType(returnType).getName();
	}

	private String[] getAliasForTokenizerType(JClassType returnType) throws UnableToCompleteException {
		PrefixAlias annotation;
		annotation = returnType.getAnnotation(PrefixAlias.class);
		if (annotation != null) {
			return annotation.value();
		}

		return null;
	}

	private Set<JClassType> getWithTokenizerEntries() {
		WithTokenizers annotation = interfaceType.getAnnotation(WithTokenizers.class);
		if (annotation == null) {
			return Collections.emptySet();
		}

		LinkedHashSet<JClassType> rtn = new LinkedHashSet<JClassType>();
		for (Class<? extends PlaceTokenizer<?>> tokenizerClass : annotation.value()) {
			JClassType tokenizerType = typeOracle.findType(tokenizerClass.getCanonicalName());
			if (tokenizerType == null) {
				logger.log(TreeLogger.ERROR, String.format("Error processing @%s, cannot find type %s", WithTokenizers.class.getSimpleName(), tokenizerClass.getCanonicalName()));
			}
			rtn.add(tokenizerType);
		}

		return rtn;
	}

	@SuppressWarnings("unused")
	private Set<JClassType> getTokenizerEntries() {
		WithTokenizers annotation = interfaceType.getAnnotation(WithTokenizers.class);
		if (annotation == null) {
			return Collections.emptySet();
		}

		JClassType[] subtype = interfaceType.getSubtypes();
		LinkedHashSet<JClassType> rtn = new LinkedHashSet<JClassType>();
		for (JClassType tokenizerType : subtype) {
			rtn.add(tokenizerType);
		}

		return rtn;
	}

	private void initTokenizersFromScan() throws UnableToCompleteException {
		JClassType[] subtypes = placeTokenizerType.getSubtypes();
		for (JClassType type : subtypes) {
			// IgnorePlace ignorePlace = tokenizerType.getAnnotation(IgnorePlace)
			if (type.isAnnotationPresent(IgnorePlace.class)) {
				continue;
			}
			if (type.isDefaultInstantiable()) {
				logger.log(Type.TRACE, type.getName());
				addPlaceTokenizer(type, getPrefixForTokenizerType(type), type);
			} else if (type.isInterface() != null && type.isAssignableTo(tokenizerType) && !tokenizerType.equals(type)) {
				logger.log(Type.TRACE, type.getName());
				addPlaceTokenizer(type, getPrefixForTokenizerType(type), type);
			} else if (!type.isAbstract()) {
				logger.log(Type.WARN, String.format(
						"The place Tokenizer '%s' isn't instantiable (must be a class, have a default constructor, be a top level class or a static nested class)",
						type.getQualifiedSourceName()));
			}
		}
	}

	private void initTokenizerGetters() throws UnableToCompleteException {
		if (factoryType != null) {

			// TODO: include non-public methods that are nevertheless accessible
			// to the interface (package-scoped);
			// Add a isCallable(JClassType) method to JAbstractMethod?
			for (JMethod method : factoryType.getInheritableMethods()) {
				if (!method.isPublic()) {
					continue;
				}
				if (method.getParameters().length > 0) {
					continue;
				}

				JClassType returnType = method.getReturnType().isClassOrInterface();

				if (returnType == null) {
					continue;
				}

				if (!placeTokenizerType.isAssignableFrom(returnType)) {
					continue;
				}

				addPlaceTokenizer(method, getPrefixForTokenizerGetter(method), method.getReturnType().isClassOrInterface());
			}
		}
	}

	private void initTokenizersWithoutGetters() throws UnableToCompleteException {
		for (JClassType tokenizerType : getWithTokenizerEntries()) {
			addPlaceTokenizer(tokenizerType, getPrefixForTokenizerType(tokenizerType), tokenizerType);
		}
	}

	private JClassType placeTypeForInterfaces(Collection<JClassType> interfaces) {
		JClassType rtn = null;
		for (JClassType i : interfaces) {
			JParameterizedType parameterizedType = i.isParameterized();
			if (parameterizedType != null && (placeTokenizerType.equals(parameterizedType.getBaseType()) || tokenizerType.equals(parameterizedType.getBaseType()))) {
				rtn = parameterizedType.getTypeArgs()[0];
			}
		}
		return rtn;
	}
}