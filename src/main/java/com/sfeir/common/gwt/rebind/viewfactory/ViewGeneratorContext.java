package com.sfeir.common.gwt.rebind.viewfactory;

import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.sfeir.common.gwt.rebind.MostToLeastDerivedPlaceTypeComparator;
import com.sfeir.common.gwt.client.mvp.View;

/**
 * Scan all views, and search the implementation of the view.
 * 
 * Search for the view name suffixed with the given suffix given in the configuration (<set-configuration-property name="clientfactory.suffixviewimpl" value="Impl" />) Fallback
 * check with the suffix Impl, and then get all subclass implemented by the view and take the first value
 * 
 * If the view interface not implement the Interface {@link com.sfeir.common.gwt.client.mvp.View} the view will not be accessible from the ClientFactory,
 */
@Deprecated
public class ViewGeneratorContext {
	public static ViewGeneratorContext create(TreeLogger logger, TypeOracle typeOracle, String suffixName, String interfaceName) throws UnableToCompleteException {
		JClassType viewType = requireType(typeOracle, View.class);
		JClassType interfaceType = typeOracle.findType(interfaceName);
		if (interfaceType == null) {
			logger.log(TreeLogger.ERROR, "Could not find requested typeName: " + interfaceName);
			throw new UnableToCompleteException();
		}

		String implName = interfaceType.getName().replace(".", "_") + "GeneratedImpl";

		return new ViewGeneratorContext(logger, typeOracle, interfaceType, viewType, suffixName, interfaceType.getPackage().getName(), implName);
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
	final JClassType viewTypes;

	final String implName;
	final String suffixName;

	final String packageName;

	/**
	 * All view types and the implementation, ordered from most-derived to least-derived type (and falling back to the natural ordering of their names).
	 */
	private TreeMap<JClassType, JClassType> viewimpls = new TreeMap<JClassType, JClassType>(new MostToLeastDerivedPlaceTypeComparator());

	ViewGeneratorContext(TreeLogger logger, TypeOracle typeOracle, JClassType interfaceType, JClassType viewType, String suffixName, String packageName, String implName) {
		this.logger = logger;
		this.typeOracle = typeOracle;
		this.interfaceType = interfaceType;
		this.viewTypes = viewType;
		this.suffixName = suffixName;
		this.packageName = packageName;
		this.implName = implName;
	}

	void ensureInitialized() throws UnableToCompleteException {
		if (viewimpls.isEmpty()) {
			initViewsImplScan();
		}
	}

	private void initViewsImplScan() throws UnableToCompleteException {
		JClassType[] subtypes = viewTypes.getSubtypes();
		for (JClassType view : subtypes) {
			// IgnorePlace ignorePlace = tokenizerType.getAnnotation(IgnorePlace)
			JClassType viewInterface = view.isInterface();
			if (viewInterface != null) {
				logger.log(Type.ALL, view.getName());
				JClassType impl = findViewImpl(viewInterface);
				if (impl != null) {
					viewimpls.put(viewInterface, impl);
				}
			}
		}
	}

	/**
	 * Check if a view implementation with a given suffix exist and implement the interface
	 * 
	 * @param interfaceType
	 *            The view Interface
	 * @param suffix
	 *            The suffix to check
	 * @return
	 */
	private JClassType getTypeFromSuffix(JClassType interfaceType, String suffix) {
		String implName = interfaceType.getQualifiedSourceName().concat(suffix);
		JClassType impl = typeOracle.findType(implName);
		logger.log(Type.TRACE, "find suffix " + suffix + " " + interfaceType.getName() + "-" + (impl != null ? impl.getQualifiedSourceName() : " not found " + implName));
		if (impl != null && !impl.isAssignableTo(interfaceType)) {
			impl = null;
		}
		return impl;
	}

	/**
	 * Find all views interface
	 * 
	 * @param interfaceType
	 * @return
	 */
	private JClassType findViewImpl(JClassType interfaceType) {
		JClassType impl = getTypeFromSuffix(interfaceType, suffixName);
		if (impl == null) {
			impl = getTypeFromSuffix(interfaceType, "Impl");
		}

		if (impl == null) {
			JClassType[] superClass = interfaceType.getSubtypes();
			for (JClassType type : superClass) {
				logger.log(Type.TRACE, "find superClass " + interfaceType.getName() + "-" + type.getName());
				if (type.isDefaultInstantiable())
					impl = type;
			}
		}
		if (impl == null)
			logger.log(Type.ERROR, "There are no implementation of the view " + interfaceType.getName());
		return impl;
	}

	public Set<Entry<JClassType, JClassType>> getViewImpl() throws UnableToCompleteException {
		ensureInitialized();
		return viewimpls.entrySet();
	}
}