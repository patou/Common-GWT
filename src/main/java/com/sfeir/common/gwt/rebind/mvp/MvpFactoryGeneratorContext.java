package com.sfeir.common.gwt.rebind.mvp;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;

public class MvpFactoryGeneratorContext {
	Multimap<String, Entry<JClassType, JClassType>> activityMap = ArrayListMultimap.create();
	Multimap<String, Entry<JClassType, JClassType>> viewMap = ArrayListMultimap.create();
	List<String> splitPts = null;
	
	public void create(TreeLogger logger, List<String> splitPts, Set<Entry<JClassType, JClassType>> listActivities, Set<Entry<JClassType, JClassType>> listView) {
		this.splitPts = Ordering.natural().reverse().sortedCopy(splitPts);
		for (Entry<JClassType, JClassType> activities : listActivities) {
			String sliptPt = getSliptPt(activities.getValue());
			//logger.log(Type.INFO, sliptPt + " - " + activities.getValue().getName());
			activityMap.put(sliptPt, activities);
		}
		for (Entry<JClassType, JClassType> views : listView) {
			String sliptPt = getSliptPt(views.getKey());
			//logger.log(Type.INFO, sliptPt + " - " + views.getKey().getName());
			viewMap.put(sliptPt, views);
		}
	}
	
	public Collection<Entry<JClassType, JClassType>> getActivityMap(String splitPt) {
		return activityMap.get(splitPt);
	}
	
	public List<String> getSplitPts() {
		return splitPts;
	}
	
	public Collection<Entry<JClassType, JClassType>> getViewMap(String splitPt) {
		return viewMap.get(splitPt);
	}

	private String getSliptPt(JClassType value) {
		for (String split : splitPts) {
			if (value.getPackage().getName().startsWith(split)) {
				return split;
			}
		}
		return null;
	}
}
