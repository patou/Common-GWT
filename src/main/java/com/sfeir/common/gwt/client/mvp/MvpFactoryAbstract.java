package com.sfeir.common.gwt.client.mvp;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class MvpFactoryAbstract implements MvpFactory {

	Map<String,MvpFactory> factories = newHashMap();
	String[] splitPts;
//	private PlaceController placeControler;
	MvpFactoryHandler handler = null;
	
	public MvpFactoryAbstract(String[] splitPts, MvpFactory defaultFactory) {
		this.splitPts = splitPts;
		handler = GWT.create(MvpFactoryHandler.class);
		setDefaultFactory(defaultFactory);
	}

	
	//For Test
	MvpFactoryAbstract() {
	}
	
	void setDefaultFactory(MvpFactory defaultFactory) {
		factories.put(null, defaultFactory);
	}

	protected String getSplitPt(String className) {
		for (String splitPt : splitPts) {
			if (className.startsWith(splitPt)) {
				return splitPt;
			}
		}
		return null;
	}
	
	protected MvpFactory getFactory(String className) {
		String splitPt = getSplitPt(className);
		GWT.log("get factory for " + className + " : " + splitPt);
		return factories.get(splitPt);
	}
	
	protected  abstract void loadFactory(String splitPt, AsyncCallback<MvpFactory> callback);
	
	protected void onLoadFactoryError(Throwable error, String splitPt) {
		handler.errorLoadsplitPt(error, splitPt);
	}
	
	@Override
	public Activity createActivity(final Place place) {
		if (Place.NOWHERE.equals(place))
			return null;
		final String splitPt = getSplitPt(place.getClass().getName());
		GWT.log("get factory for " + place.getClass().getName() + " : " + splitPt);
		MvpFactory factory = factories.get(splitPt);
		if (factory != null) {
			return factory.createActivity(place);
		}
		if (splitPt != null) {
			final LoadingSplitActivity<?> activity = LoadingSplitActivity.create(place);
			GWT.log("load factory for " + splitPt);
			handler.startLoadsplitPt(splitPt);
			loadFactory(splitPt, new AsyncCallback<MvpFactory>() {

				@Override
				public void onFailure(Throwable caught) {
					onLoadFactoryError(caught, splitPt);
				}

				@Override
				public void onSuccess(MvpFactory factory) {
					GWT.log("loaded factory for " + splitPt);
					factories.put(splitPt, factory);
					handler.endLoadsplitPt(splitPt);
					Activity createActivity = factory.createActivity(place);
					if (createActivity != null) {
						activity.setActivityLoaded(createActivity);
						GWT.log("start loaded activity " + createActivity.getClass().getName());
					}
					else
						activity.setErrorNoActivity("No activity loaded in the factory for "+ place.getClass().getName());
				}
			});
			GWT.log("return loading activity");
			return activity;
		}
		return null;
	}
	
	public void getActivity(final Place place, final AsyncCallback<Activity> callback) {
		final String splitPt = getSplitPt(place.getClass().getName());
		MvpFactory factory = factories.get(splitPt);
		if (factory != null) {
			callback.onSuccess(factory.createActivity(place));
		}
		if (splitPt != null) {
			GWT.log("load factory " + splitPt);
			loadFactory(splitPt, new AsyncCallback<MvpFactory>() {

				@Override
				public void onFailure(Throwable caught) {
					if (callback != null)
						callback.onFailure(caught);
				}

				@Override
				public void onSuccess(MvpFactory factory) {
					GWT.log("loaded factory " + splitPt);
					factories.put(splitPt, factory);
					if (callback != null)
						callback.onSuccess(factory.createActivity(place));
				}
			});
		}
	}

	@Override
	public View createView(String viewClass) {
		MvpFactory factory = getFactory(viewClass);
		if (factory != null) {
			return factory.createView(viewClass);
		}
		GWT.log("no mvp factory for " + viewClass.getClass().getName());
		handler.errorNoFactory(viewClass);
		return null;
	}
}
