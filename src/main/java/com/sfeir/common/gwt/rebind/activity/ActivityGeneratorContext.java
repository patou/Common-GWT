package com.sfeir.common.gwt.rebind.activity;

import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.sfeir.common.gwt.rebind.MostToLeastDerivedPlaceTypeComparator;
import com.google.gwt.place.shared.Place;
import com.sfeir.common.gwt.client.mvp.ActivityPresenter;

/**
 * Scan all Activity subclass and try to determinate the corresponding Place If the Activity extend ActivityPresenter, get the corresponding place from the parameter Otherwise find
 * a Place with the Activity class name with replace the Suffix "Activity" by "Place" (ie LoginActivity => LoginPlace) It will find it in the same package first, and if not found,
 * in other package
 */
@Deprecated
class ActivityGeneratorContext {
	static ActivityGeneratorContext create(TreeLogger logger, TypeOracle typeOracle, String interfaceName) throws UnableToCompleteException {
		JClassType activityPresenterType = requireType(typeOracle, ActivityPresenter.class);
		JClassType placeType = requireType(typeOracle, Place.class);
		JClassType activityType = requireType(typeOracle, Activity.class);
		JClassType interfaceType = typeOracle.findType(interfaceName);
		if (interfaceType == null) {
			logger.log(TreeLogger.ERROR, "Could not find requested typeName: " + interfaceName);
			throw new UnableToCompleteException();
		}

		String implName = interfaceType.getName().replace(".", "_") + "Impl";

		return new ActivityGeneratorContext(logger, typeOracle, interfaceType, activityPresenterType, activityType, placeType, interfaceType.getPackage().getName(), implName);
	}

	private static JClassType findPlaceTypeFromActivityPresenter(JClassType activityPresenterType, JClassType activityType) {
		for (JClassType supertype : activityType.getFlattenedSupertypeHierarchy()) {
			JParameterizedType parameterizedType = supertype.isParameterized();
			if (parameterizedType != null && activityPresenterType.equals(parameterizedType.getBaseType())) {
				JClassType jClassType = parameterizedType.getTypeArgs()[0];
				return jClassType;
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
	final JClassType placeType;
	final JClassType activityPresenterType;
	final JClassType activityType;

	final String implName;

	final String packageName;

	/**
	 * All place types and the activity of their associated place, ordered from most-derived to least-derived type (and falling back to the natural ordering of their names).
	 */
	private TreeMap<JClassType, JClassType> activities = new TreeMap<JClassType, JClassType>(new MostToLeastDerivedPlaceTypeComparator());

	ActivityGeneratorContext(TreeLogger logger, TypeOracle typeOracle, JClassType interfaceType, JClassType activityPresenterType, JClassType activityType, JClassType placeType,
			String packageName, String implName) {
		this.logger = logger;
		this.typeOracle = typeOracle;
		this.interfaceType = interfaceType;
		this.activityPresenterType = activityPresenterType;
		this.activityType = activityType;
		this.placeType = placeType;
		this.packageName = packageName;
		this.implName = implName;
	}

	void ensureInitialized() throws UnableToCompleteException {
		if (activities.isEmpty()) {
			initActivities();
		}
	}

	private void initActivities() throws UnableToCompleteException {
		JClassType[] subtypes = activityType.getSubtypes();
		for (JClassType activityType : subtypes) {
			// IgnorePlace ignorePlace = tokenizerType.getAnnotation(IgnorePlace)
			if (activityType.isDefaultInstantiable()) {
				logger.log(Type.ALL, activityType.getName());
				JClassType place = findPlaceTypeFromActivityPresenter(activityPresenterType, activityType);
				if (place == null) {
					place = findPlaceFromActivityName(activityType);
				}
				if (place != null) {
					activities.put(place, activityType);
				} else {
					logger.log(TreeLogger.ERROR, String.format("Unable to find the corresponding place for the Activity %s", activityType.getQualifiedSourceName()));
				}
			} else {
				logger.log(TreeLogger.TRACE, "The activity '" + activityType.getName()
						+ "' is not instantiable (must be a class, have a default constructor, be a top level class or a static nested class)");
			}
		}
	}

	private JClassType findPlaceFromActivityName(JClassType activityType) {
		JClassType place = null;
		// find the Place package.NamePlace exists from package.NameActivity
		String packagePlaceName = activityType.getQualifiedSourceName().replace("Activity", "Place");
		place = typeOracle.findType(packagePlaceName);
		if (place != null && !place.isAssignableTo(placeType)) {
			place = null;
		}
		// Find in all Place subtype if there are a NamePlace from another package, take the first class found
		if (place == null) {
			String placeName = activityType.getSimpleSourceName().replace("Activity", "Place");
			for (JClassType subPlace : placeType.getSubtypes()) {
				if (subPlace.getName().equals(placeName)) {
					place = subPlace;
					break;
				}
			}
		}
		return place;
	}

	public Set<Entry<JClassType, JClassType>> getActivities() throws UnableToCompleteException {
		ensureInitialized();
		return activities.entrySet();
	}
}