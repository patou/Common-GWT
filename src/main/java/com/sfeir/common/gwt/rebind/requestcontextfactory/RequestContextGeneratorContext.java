package com.sfeir.common.gwt.rebind.requestcontextfactory;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

/**
 * Scan all GWT-RPC services, and search the async interface.
 * 
 */
class RequestContextGeneratorContext {
	static RequestContextGeneratorContext create(TreeLogger logger, TypeOracle typeOracle, String interfaceName) throws UnableToCompleteException {
		JClassType requestFactoryType = requireType(typeOracle, RequestFactory.class);
		JClassType requestContextType = requireType(typeOracle, RequestContext.class);
		JClassType interfaceType = typeOracle.findType(interfaceName);
		if (interfaceType == null) {
			logger.log(TreeLogger.ERROR, "Could not find requested typeName: " + interfaceName);
			throw new UnableToCompleteException();
		}

		String implName = interfaceType.getName().replace(".", "_") + "GeneratedImpl";

		return new RequestContextGeneratorContext(logger, typeOracle, interfaceType, requestFactoryType, requestContextType, interfaceType.getPackage().getName(), implName);
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
	final JClassType requestFactoryType;
	final JClassType requestContextType;

	final String implName;

	final String packageName;

	/**
	 * All service types and the implementation, ordered from most-derived to least-derived type (and falling back to the natural ordering of their names).
	 */
	private HashMap<JClassType, JMethod> requestContextMethod = newHashMap();
	private List<JClassType> requestFactoryEntries = newArrayList();

	RequestContextGeneratorContext(TreeLogger logger, TypeOracle typeOracle, JClassType interfaceType, JClassType requestFactoryType, JClassType requestContextType, String packageName, String implName) {
		this.logger = logger;
		this.typeOracle = typeOracle;
		this.interfaceType = interfaceType;
		this.requestFactoryType = requestFactoryType;
		this.requestContextType = requestContextType;
		this.packageName = packageName;
		this.implName = implName;
	}

	void ensureInitialized() throws UnableToCompleteException {
		if (requestFactoryEntries.isEmpty()) {
			initRequestFactoryTypeScan();
		}
	}

	private void initRequestFactoryTypeScan() throws UnableToCompleteException {
		JClassType[] subtypes = requestFactoryType.getSubtypes();
		boolean hasErrors = false;
		for (JClassType rf : subtypes) {
			if (rf.isInterface() == null) //Remove implementation of the RequestFactory
				continue;
			requestFactoryEntries.add(rf);
			for (JMethod method : rf.getMethods()) {
				JClassType type = method.getReturnType().isInterface();
				if (type != null && type.isAssignableTo(requestContextType) && method.getParameters().length == 0) {
					requestContextMethod.put(type, method);
				}
				else {
					logger.log(Type.TRACE, String.format("the method %s.%s() didn't return a RequestContext interface or have parameters", rf.getName(), method.getName()));
				}
			}
		}
		if (hasErrors)
			throw new UnableToCompleteException();
	}
	
	public List<JClassType> getRequestFactoryEntries() throws UnableToCompleteException {
		ensureInitialized();
		return requestFactoryEntries;
	}
	
	public Set<Entry<JClassType, JMethod>> getRequestContextMethod() throws UnableToCompleteException {
		ensureInitialized();
		return requestContextMethod.entrySet();
	}
}