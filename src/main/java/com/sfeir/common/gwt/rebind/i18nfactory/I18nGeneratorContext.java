package com.sfeir.common.gwt.rebind.i18nfactory;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.i18n.client.LocalizableResource;

/**
 * Scan all I18n LocalizableReource
 * 
 */
class I18nGeneratorContext {
	static I18nGeneratorContext create(TreeLogger logger,
			TypeOracle typeOracle, String interfaceName)
			throws UnableToCompleteException {
		JClassType localizableResourceType = requireType(typeOracle,
				LocalizableResource.class);
		JClassType interfaceType = typeOracle.findType(interfaceName);
		if (interfaceType == null) {
			logger.log(TreeLogger.ERROR, "Could not find requested typeName: "
					+ interfaceName);
			throw new UnableToCompleteException();
		}

		String implName = interfaceType.getName().replace(".", "_")
				+ "GeneratedImpl";

		return new I18nGeneratorContext(logger, typeOracle, interfaceType,
				localizableResourceType, interfaceType.getPackage().getName(),
				implName);
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
	final JClassType localizableResourceType;

	final String implName;

	final String packageName;

	private List<JClassType> localizableResources = new ArrayList<JClassType>();

	I18nGeneratorContext(TreeLogger logger, TypeOracle typeOracle,
			JClassType interfaceType, JClassType localizableResourceType,
			String packageName, String implName) {
		this.logger = logger;
		this.typeOracle = typeOracle;
		this.interfaceType = interfaceType;
		this.localizableResourceType = localizableResourceType;
		this.packageName = packageName;
		this.implName = implName;
	}

	void ensureInitialized() throws UnableToCompleteException {
		if (localizableResources.isEmpty()) {
			initlocalizableResourcesTypeScan();
		}
	}

	/**
	 * Scan all Messages, Contants or ContantsWithLookup
	 * @throws UnableToCompleteException
	 */
	private void initlocalizableResourcesTypeScan() throws UnableToCompleteException {
		JClassType[] subtypes = localizableResourceType.getSubtypes();
		boolean hasErrors = false;
		for (JClassType localizableResource : subtypes) {
			hasErrors = false;
			String name = localizableResource.getQualifiedSourceName();
			if (name != null && name.startsWith("com.google.gwt")) //Mask GWT Resources
				continue;
			if (localizableResource.isInterface() == null) {
				/*logger.log(
						Type.ERROR,
						String.format(
								"The LocalizableResources %s was not an interface",
								name));
				throw new UnableToCompleteException();*/
				hasErrors = true; //TODO
			}
			//TODO find property files, and warn if not present ???
			if (!hasErrors)
				localizableResources.add(localizableResource);
		}
		if (hasErrors)
			throw new UnableToCompleteException();
	}

	public List<JClassType> getlocalizableResources()
			throws UnableToCompleteException {
		ensureInitialized();
		return localizableResources;
	}
}