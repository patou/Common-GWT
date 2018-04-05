package com.sfeir.common.gwt.rebind.servicefactory;

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
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Scan all GWT-RPC services, and search the async interface.
 * 
 */
class ServiceGeneratorContext {
	static ServiceGeneratorContext create(TreeLogger logger, TypeOracle typeOracle, String interfaceName) throws UnableToCompleteException {
		JClassType remoteServiceType = requireType(typeOracle, RemoteService.class);
		JClassType interfaceType = typeOracle.findType(interfaceName);
		if (interfaceType == null) {
			logger.log(TreeLogger.ERROR, "Could not find requested typeName: " + interfaceName);
			throw new UnableToCompleteException();
		}

		String implName = interfaceType.getName().replace(".", "_") + "GeneratedImpl";

		return new ServiceGeneratorContext(logger, typeOracle, interfaceType, remoteServiceType, interfaceType.getPackage().getName(), implName);
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
	final JClassType remoteServiceType;

	final String implName;

	final String packageName;

	/**
	 * All service types and the implementation, ordered from most-derived to least-derived type (and falling back to the natural ordering of their names).
	 */
	private TreeMap<JClassType, JClassType> servicesAsync = new TreeMap<JClassType, JClassType>(new MostToLeastDerivedPlaceTypeComparator());

	ServiceGeneratorContext(TreeLogger logger, TypeOracle typeOracle, JClassType interfaceType, JClassType remoteServiceType, String packageName, String implName) {
		this.logger = logger;
		this.typeOracle = typeOracle;
		this.interfaceType = interfaceType;
		this.remoteServiceType = remoteServiceType;
		this.packageName = packageName;
		this.implName = implName;
	}

	void ensureInitialized() throws UnableToCompleteException {
		if (servicesAsync.isEmpty()) {
			initRemoteServiceTypeScan();
		}
	}

	private void initRemoteServiceTypeScan() throws UnableToCompleteException {
		JClassType[] subtypes = remoteServiceType.getSubtypes();
		boolean hasErrors = false;
		for (JClassType service : subtypes) {
			if (service.getPackage().getName().equals("com.google.gwt.user.client.rpc")) // Remove error from GWT Interface Xsrf*Service
				continue;
			String name = service.getQualifiedSourceName();
			if (service.isInterface() == null) {
				logger.log(Type.ERROR, String.format("The GWT-RPC service %s was not an interface or the server implementation is accessible in the GWT code", name));
				hasErrors = true;
			}

			if (!service.isAnnotationPresent(RemoteServiceRelativePath.class)) {
				logger.log(Type.ERROR, String.format("The GWT-RPC service %s hasn't the @RemoteServiceRelativePath annotation", name));
				hasErrors = true;
			}

			JClassType serviceAsync = typeOracle.findType(name.concat("Async"));
			if (serviceAsync == null) {
				logger.log(Type.ERROR, String.format("You must create the service async %s for the GWT-RPC service %s ", name.concat("Async"), name));
				hasErrors = true;
			}

			if (!hasErrors)
				servicesAsync.put(service, serviceAsync);
		}
		if (hasErrors)
			throw new UnableToCompleteException();
	}

	public Set<Entry<JClassType, JClassType>> getServicesAsync() throws UnableToCompleteException {
		ensureInitialized();
		return servicesAsync.entrySet();
	}
}